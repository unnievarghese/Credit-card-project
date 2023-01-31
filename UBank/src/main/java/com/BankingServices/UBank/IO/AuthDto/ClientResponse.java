package com.BankingServices.UBank.IO.AuthDto;

import com.BankingServices.UBank.Enum.AccountDetails;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class ClientResponse {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Date dob;

    private String email;

    private AccountDetails.Gender gender;

    private String fatherName;

    private String motherName;

    private AccountDetails.MaritalStatus maritalStatus;

    private AccountDetails.EducationalQualification educationalQualification;

    private String panNumber;

    private String nameOnCard;

    private String crn;

    private List<ClientJobResponse> clientJobDetail;

    private List<ClientAddressResponse> clientAddresses;
}
