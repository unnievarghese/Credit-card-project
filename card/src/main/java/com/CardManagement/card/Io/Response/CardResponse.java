package com.CardManagement.card.Io.Response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CardResponse {

    private String cardNumber;

    private String crn;

    private LocalDate expiry;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private String cvv;

    private Float apr;

    private Float limit;

    private Float balance;

    private LocalDate billingDate;

    private Integer gracePeriod;

    private Integer minimumPaymentPercent;

    private Boolean isDue;
}
