package com.UBank.Auth.IO.Response;

import com.UBank.Auth.Enum.ClientDetails;
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

    private ClientDetails.Gender gender;

    private String fatherName;

    private String motherName;

    private ClientDetails.MaritalStatus maritalStatus;

    private ClientDetails.EducationalQualification educationalQualification;

    private String panNumber;

    private String nameOnCard;

    private String crn;

    private List<ClientJobResponse> clientJobDetail;

    private List<ClientAddressResponse> clientAddresses;
}
