package com.BankingServices.UBank.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_details")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "transaction_id")
    private long transactionId;

    @Column(name = "sourceAccountId")
    private String sourceAccountId;

    @Column(name = "targetAccountId")
    private String targetAccountId;

    @Column(name = "targetOwnerName")
    private String targetOwnerName;

    @Column(name = "amount")
    private double amount;

    @Column(name = "initiationDate")
    private LocalDateTime initiationDate;

    @Column(name = "completionDate")
    private LocalDateTime completionDate;

    @Column(name = "reference")
    private String reference;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @ManyToMany(mappedBy = "transactions")
    private List<Account> accounts;
}
