package com.UBank.Auth.IO.Response;

import com.UBank.Auth.Enum.ClientDetails;
import lombok.Data;

@Data
public class ClientJobResponse {

    private String monthlyIncome;

    private ClientDetails.OccupationalType occupationalType;

    private String designation;

    private String firmName;

    private String workEmail;
}
