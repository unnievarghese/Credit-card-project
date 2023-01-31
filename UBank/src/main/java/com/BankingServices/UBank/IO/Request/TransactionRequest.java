package com.BankingServices.UBank.IO.Request;

import lombok.Data;

@Data
public class TransactionRequest {

    private String sourceAccountNumber;

    private String targetAccountNumber;

    private String targetOwnerName;

    private Float amount;

    private String  latitude;

    private String  longitude;
}
