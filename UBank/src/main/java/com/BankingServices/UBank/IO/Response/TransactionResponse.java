package com.BankingServices.UBank.IO.Response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private String sourceAccountId;

    private String targetAccountId;

    private String targetOwnerName;

    private Float amount;

    private LocalDateTime initiationDate;

    private LocalDateTime completionDate;

    private String reference;

    private String latitude;

    private String longitude;

}
