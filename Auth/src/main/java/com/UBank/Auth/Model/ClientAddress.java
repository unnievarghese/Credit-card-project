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
@Table(name = "client_address")
public class ClientAddress implements Serializable {

    private static final long serialVersionUID = -5410300459457318131L;

    @Id
    @GeneratedValue
    @Column(name = "clientAddress_id")
    private Integer clientAddressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client clientDetail;

    @Column(name = "houseNumber")
    private String houseNumber;

    @Column(name = "area")
    private String area;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;

    @Column(name = "addressType")
    private AddressType addressType = AddressType.PERMANENT;
}
