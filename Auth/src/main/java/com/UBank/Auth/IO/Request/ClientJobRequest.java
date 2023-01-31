package com.UBank.Auth.IO.Request;

import com.UBank.Auth.Enum.ClientDetails;
import lombok.Data;

@Data
public class ClientJobRequest {

    private String monthlyIncome;

    private ClientDetails.OccupationalType occupationalType;

    private String designation;

    private String firmName;

    private String workEmail;
}
