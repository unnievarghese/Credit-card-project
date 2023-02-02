package com.UBank.Auth.IO.Request;

import com.UBank.Auth.Enum.ClientDetails;
import com.UBank.Auth.Model.ClientAddress;
import com.UBank.Auth.Model.ClientJobDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ClientDetailsRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dob;

    private String email;

    private String password;

    private ClientDetails.Gender gender;

    private String fatherName;

    private String motherName;

    private ClientDetails.MaritalStatus maritalStatus;

    private ClientDetails.EducationalQualification educationalQualification;

    private String panNumber;

    private String nameOnCard;

    private List<ClientJobDetail> clientJobDetail;

    private List<ClientAddress> clientAddresses;
}
