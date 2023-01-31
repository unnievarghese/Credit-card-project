package com.CardManagement.card.Io.AuthDto;

import com.CardManagement.card.Enum.CardDetails;
import lombok.Data;

@Data
public class ClientJobResponse {

    private String monthlyIncome;

    private CardDetails.OccupationalType occupationalType;

    private String designation;

    private String firmName;

    private String workEmail;
}
