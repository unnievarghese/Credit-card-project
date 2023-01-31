package com.BankingServices.UBank.IO.Response;

import com.BankingServices.UBank.Model.Transaction;
import lombok.Data;
import java.util.List;

@Data
public class AccountResponse {

    private String accountNumber;

    private Float currentBalance;

    private String crn;

    private transient TransactionResponse transactions;

    private Boolean isAutoPaymentAllowed;
}
