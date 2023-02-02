package com.BankingServices.UBank.AutheticateClient;

import com.BankingServices.UBank.IO.AuthDto.ClientResponse;
import com.BankingServices.UBank.IO.Request.TransactionRequest;
import com.BankingServices.UBank.Model.Account;
import com.BankingServices.UBank.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Objects;

public class AuthenticateClient {

    @Autowired
    AccountRepository accountRepository;

    public Boolean checkClientAuthentication(TransactionRequest transactionRequest, ClientResponse clientData){

        Boolean status = false;
        Account account = accountRepository.findByAccountNumber(transactionRequest.getSourceAccountNumber());
        if(!Objects.equals(account.getCrn(), clientData.getCrn()))
            return true;
        return status;
    }
}
