package com.BankingServices.UBank.Service;

import com.BankingServices.UBank.AutheticateClient.AuthenticateClient;
import com.BankingServices.UBank.Enum.AccountDetails.*;
import com.BankingServices.UBank.FeignClient.AuthClient;
import com.BankingServices.UBank.FeignClient.CardClient;
import com.BankingServices.UBank.IO.AuthDto.ClientResponse;
import com.BankingServices.UBank.IO.Request.TransactionRequest;
import com.BankingServices.UBank.IO.Response.AccountResponse;
import com.BankingServices.UBank.IO.Response.TransactionResponse;
import com.BankingServices.UBank.Model.Account;
import com.BankingServices.UBank.Model.Transaction;
import com.BankingServices.UBank.Repository.AccountRepository;
import com.BankingServices.UBank.Repository.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    AuthClient authClient;

    @Autowired
    CardClient cardClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticateClient authenticateClient;

    public AccountResponse makeTransaction(TransactionRequest transactionRequest, HttpServletRequest req) throws Exception {
        ClientResponse clientData = getClientDetails(req);
        String crn = clientData.getCrn().isEmpty() ? null : clientData.getCrn();
        if(authenticateClient.checkClientAuthentication(transactionRequest, clientData))
            throw new Exception("No Authentication to access the account!");
        Float currentBalance = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber()).getCurrentBalance();
        if(transactionRequest.getAmount() <= 0 || currentBalance < transactionRequest.getAmount())
            throw new Exception("no balance");
        Transaction transaction = saveTransaction(createTransactionObject(crn, transactionRequest));
        Account updatedAccount = updateAccounts(currentBalance, Action.DEPOSIT_WITHDRAW, transactionRequest, transaction);
        return convertToAccountResponse(updatedAccount, transaction);
    }

    public AccountResponse deposit (TransactionRequest transactionRequest, HttpServletRequest req) throws Exception{
        String crn = "cash deposit";
        Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
        Float currentBalance = account.getCurrentBalance();
        if(transactionRequest.getAmount() < 0)
            throw new Exception("minimum deposit amount is 1.");
        Transaction transaction = saveTransaction(createTransactionObject(crn, transactionRequest));
        Account updatedAccount = updateAccounts(currentBalance, Action.DEPOSIT, transactionRequest, transaction);
        return convertToAccountResponse(updatedAccount, transaction);
    }

    public AccountResponse withdrawal (TransactionRequest transactionRequest, HttpServletRequest req) throws Exception{
        String crn ="cash withdrawal";
        ClientResponse clientData = getClientDetails(req);
        if(authenticateClient.checkClientAuthentication(transactionRequest, clientData))
            throw new Exception("No Authentication to access the account!");
        Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
        Float currentBalance = account.getCurrentBalance();
        if(transactionRequest.getAmount() > currentBalance)
            throw new Exception("insufficient balance.");
        Transaction transaction = saveTransaction(createTransactionObject(crn, transactionRequest));
        Account updatedAccount = updateAccounts(currentBalance, Action.WITHDRAW, transactionRequest, transaction);
        return convertToAccountResponse(updatedAccount, transaction);
    }

    public Boolean handleCreditCardPayment(TransactionRequest transactionRequest){
        String crn ="creditCard payment";
        Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
        if(!account.getIsAutoPaymentAllowed())
            return false;
        Float currentBalance = account.getCurrentBalance();
        if(transactionRequest.getAmount() > currentBalance)
            return false;
        Transaction transaction = saveTransaction(createTransactionObject(crn, transactionRequest));
        updateAccounts(currentBalance, Action.WITHDRAW, transactionRequest, transaction);
        return true;
    }

    public AccountResponse makeCreditCardPayment(TransactionRequest transactionRequest, String cardNumber, HttpServletRequest req){
        String crn ="creditCard payment";
        LinkedHashMap response = cardClient.makeCreditCardPayment(transactionRequest, cardNumber, req.getHeader("Authorization"));
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        Boolean status =  (Boolean) responseData.get("data");
        if(status) {
            Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
            Transaction transaction = saveTransaction(createTransactionObject(crn, transactionRequest));
            Float currentBalance = account.getCurrentBalance();
            return convertToAccountResponse(updateAccounts(currentBalance, Action.WITHDRAW, transactionRequest, transaction), transaction);
        }
        return new AccountResponse();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public TransactionResponse convertToTransactionResponse(Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        modelMapper.map(transaction, transactionResponse);
        return transactionResponse;
    }

    public AccountResponse convertToAccountResponse(Account account, Transaction transaction) {
        AccountResponse accountResponse = new AccountResponse();
        modelMapper.map(account, accountResponse);
        accountResponse.setTransactions(convertToTransactionResponse(transaction));
        return accountResponse;
    }

    public ClientResponse getClientDetails(HttpServletRequest req){
        LinkedHashMap response = authClient.getClientDetails(req.getHeader("Authorization"));
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        return objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class);
    }

    public Account updateAccounts(Float currentBalance, Action action, TransactionRequest transactionRequest, Transaction transaction){
        Account updatedAccount = new Account();
        if(action == Action.DEPOSIT) {
            Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
            account.setCurrentBalance(currentBalance + transactionRequest.getAmount());
            addTransaction(account, transaction);
            updatedAccount = saveAccount(account);
        }
        if(action == Action.WITHDRAW) {
            Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
            account.setCurrentBalance(currentBalance - transactionRequest.getAmount());
            addTransaction(account, transaction);
            updatedAccount = saveAccount(account);
        }
        if(action == Action.DEPOSIT_WITHDRAW){
            Account sourceAccount = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
            Float reducedBalance = currentBalance - transactionRequest.getAmount();
            sourceAccount.setCurrentBalance(reducedBalance);
            addTransaction(sourceAccount, transaction);
            updatedAccount = saveAccount(sourceAccount);

            Account targetAccount = accountRepository.findByAccountNumber(transactionRequest.getTargetAccountNumber());
            Float increasedBalance = targetAccount.getCurrentBalance() + transactionRequest.getAmount();
            targetAccount.setCurrentBalance(increasedBalance);
            addTransaction(targetAccount, transaction);
            saveAccount(targetAccount);
        }
        return updatedAccount;
    }

    public void addTransaction(Account account, Transaction transaction){
        List<Transaction> transactions = account.getTransactions();
        transactions.add(transaction);
        account.setTransactions(transactions);
    }

    public Transaction createTransactionObject(String crn, TransactionRequest transactionRequest){
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setLatitude(transactionRequest.getLatitude());
        transaction.setLongitude(transactionRequest.getLongitude());
        transaction.setInitiationDate(LocalDateTime.now());
        transaction.setCompletionDate(LocalDateTime.now());
        transaction.setReference(crn +"_"+ LocalDateTime.now());
        transaction.setSourceAccountId(transactionRequest.getSourceAccountNumber());
        transaction.setTargetAccountId(transactionRequest.getTargetAccountNumber());
        transaction.setTargetOwnerName(transactionRequest.getTargetOwnerName());
        return transaction;
    }
}
