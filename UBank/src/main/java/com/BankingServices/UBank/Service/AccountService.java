package com.BankingServices.UBank.Service;

import com.BankingServices.UBank.FeignClient.AuthClient;
import com.BankingServices.UBank.IO.AuthDto.ClientResponse;
import com.BankingServices.UBank.IO.Response.AccountResponse;
import com.BankingServices.UBank.Model.Account;
import com.BankingServices.UBank.Repository.AccountRepository;
import com.BankingServices.UBank.Util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class AccountService {

    @Autowired
    AuthClient authClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Utils utils;

    public AccountResponse createAccount(HttpServletRequest req){
        LinkedHashMap response = authClient.getClientDetails(req.getHeader("Authorization"));
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        ClientResponse clientData = objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class);
        Account account = save(createAccountObject(clientData));
        return convertToResponse(account);
    }

    public AccountResponse checkBalance(String accountNumber, HttpServletRequest req) throws Exception {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        ClientResponse clientData = getClientDetails(req);
        if(!Objects.equals(clientData.getCrn(), account.getCrn()))
            throw new Exception("No Authentication to access the account!");
        return convertToResponse(account);
    }

    public AccountResponse convertToResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        modelMapper.map(account, accountResponse);
        return accountResponse;
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Account createAccountObject(ClientResponse clientData){
        Account account = new Account();
        account.setAccountNumber(utils.generateRandomString(11));
        account.setCrn(clientData.getCrn());
        account.setCurrentBalance(0F);
        account.setIsAutoPaymentAllowed(false);
        return account;
    }

    public ClientResponse getClientDetails(HttpServletRequest req){
        LinkedHashMap response = authClient.getClientDetails(req.getHeader("Authorization"));
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        return objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class);
    }
}
