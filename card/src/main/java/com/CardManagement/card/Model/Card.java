package com.CardManagement.card.Model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "card_details")
public class Card implements Serializable {

    private static final long serialVersionUID = -1775007869105407122L;

    @Id
    @GeneratedValue
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "cardNumber")
    private String cardNumber;

    @Column(name = "ClientReferenceNumber")
    private String crn;

    @Column(name = "expiry")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate expiry;

    @Column(name = "createdAt")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate createdAt;

    @Column(name = "updatedAt")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate updatedAt;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "pin")
    private String encryptedPin;

    @Column (name = "serviceCode")
    private String serviceCode;

    @Column(name = "apr")
    private Float apr;

    @Column(name = "creditLimit")
    private Float creditLimit;

    @Column(name = "balance")
    private Float balance;

    @Column(name = "billingDate")
    private LocalDate billingDate;

    @Column(name = "gracePeriod")
    private Integer gracePeriod;

    @Column(name = "minimumPaymentPercent")
    private Float minimumPaymentPercent;

    @Column(name = "isDue")
    private Boolean isDue;


    @Column(name = "isDeactivated")
    private Boolean isDeactivated;

    @OneToMany(mappedBy="cardDetails",fetch = FetchType.EAGER)
    private List<Ledger> activities;

    @OneToMany(mappedBy="cardDetails",fetch = FetchType.EAGER)
    private List<Bill> bills;

}
