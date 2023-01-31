package com.UBank.Auth.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.UBank.Auth.Enum.ClientDetails.*;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_job_details")
public class ClientJobDetail implements Serializable {

    private static final long serialVersionUID = -297702145801936835L;

    @Id
    @GeneratedValue
    @Column(name = "client_Job_id")
    private Integer clientJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client clientDetail;

    @Column(name = "monthlyIncome")
    private String monthlyIncome;

    @Column(name = "occupationalType")
    private OccupationalType occupationalType = OccupationalType.SALARIED;

    @Column(name = "designation")
    private String designation;

    @Column(name = "firmName")
    private String firmName;

    @Column(name = "workEmail")
    private String workEmail;
}
