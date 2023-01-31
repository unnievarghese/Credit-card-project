package com.CardManagement.card.Io.AuthDto;

import com.CardManagement.card.Enum.CardDetails;
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

    private CardDetails.Gender gender;

    private String fatherName;

    private String motherName;

    private CardDetails.MaritalStatus maritalStatus;

    private CardDetails.EducationalQualification educationalQualification;

    private String panNumber;

    private String nameOnCard;

    private String crn;

    private List<ClientJobResponse> clientJobDetail;

    private List<ClientAddressResponse> clientAddresses;
}
