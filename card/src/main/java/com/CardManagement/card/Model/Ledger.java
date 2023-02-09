package com.CardManagement.card.Model;

import com.CardManagement.card.Enum.CardDetails.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity")
public class Ledger implements Serializable {

    private static final long serialVersionUID = 3874754463294681918L;

    @Id
    @GeneratedValue
    @Column(name = "activity_id")
    private Integer activityId;

    @Column(name = "date")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private LocalDate date;

    @Column(name = "paymentType")
    private PaymentType paymentType;

    @Column(name = "amount")
    private Float amount;

    @ManyToOne
    @JoinColumn(name="card_id")
    private Card cardDetails;

    @ManyToOne
    @JoinColumn(name="bill_id")
    private Bill bill;

    @Column(name = "reference")
    private String reference;
}
