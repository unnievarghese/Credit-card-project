package com.CardManagement.card.Model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill implements Serializable {

    private static final long serialVersionUID = 5598170867545068052L;

    @Id
    @GeneratedValue
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "reference")
    private String reference;

    @Column(name = "balance")
    private String balance;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "apr")
    private Float apr;

    @OneToMany(mappedBy="bill")
    private List<Activity> purchaseList;
}
