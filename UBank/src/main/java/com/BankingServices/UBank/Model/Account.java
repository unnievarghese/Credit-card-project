package com.BankingServices.UBank.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_details")
public class Account implements Serializable {

    private static final long serialVersionUID = 3271293646896157911L;

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "accountNumber")
    private String accountNumber;

    @Column(name = "currentBalance")
    private Float currentBalance;

    @Column(name = "crn")
    private String crn;

    @ManyToMany
    @JoinTable(name = "transactions_list",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private List<Transaction> transactions;

    @Column(name = "isAuto")
    private Boolean isAutoPaymentAllowed;
}
