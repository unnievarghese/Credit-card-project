package com.BankingServices.UBank.IO.AuthDto;

import com.BankingServices.UBank.Enum.AccountDetails;
import lombok.Data;

@Data
public class ClientJobResponse {

    private String monthlyIncome;

    private AccountDetails.OccupationalType occupationalType;

    private String designation;

    private String firmName;

    private String workEmail;
}
