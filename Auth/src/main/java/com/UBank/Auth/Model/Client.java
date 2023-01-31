package com.UBank.Auth.Model;

import com.UBank.Auth.Enum.ClientDetails.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client_details")
public class Client implements Serializable {

    private static final long serialVersionUID = -603637646017144231L;

    @Id
    @GeneratedValue
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "phone")
    private String phoneNumber;

    @Column(name = "dob")
    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Date dob;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String encryptedPassword;

    @Column(name = "gender")
    private Gender gender = Gender.MALE;

    @Column(name = "fatherName")
    private String fatherName;

    @Column(name = "motherName")
    private String motherName;

    @Column(name = "maritalStatus")
    private MaritalStatus maritalStatus = MaritalStatus.SINGLE;

    @Column(name = "educationalQualification")
    private EducationalQualification educationalQualification = EducationalQualification.GRADUATE;

    @Column(name = "panNumber")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}")
    private String panNumber;

    @Column(name = "nameOnCard")
    private String nameOnCard;

    @OneToMany(mappedBy = "clientDetail", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ClientJobDetail> clientJobDetail;

    @OneToMany(mappedBy = "clientDetail", cascade = CascadeType.ALL)
    private List<ClientAddress> clientAddresses;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="client_roles",
            joinColumns = @JoinColumn(name="client_id",referencedColumnName = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
    private Collection<Role> roles;

    @Column(name = "ClientReferenceNumber")
    private String crn;
}
