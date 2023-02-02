package com.CardManagement.card.Io.Request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateRequest {

    LocalDate expiry;

    String cvv;

    Float apr;

    Float creditLimit;

    Float balance;

    LocalDate billingDate;

    Integer gracePeriod;

    Float minimumPaymentPercent;

    Boolean isDue;

}
