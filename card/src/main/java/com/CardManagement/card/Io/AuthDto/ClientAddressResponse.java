package com.CardManagement.card.Io.AuthDto;

import com.CardManagement.card.Enum.CardDetails;
import lombok.Data;

@Data
public class ClientAddressResponse {

    private String houseNumber;

    private String area;

    private String city;

    private String district;

    private String state;

    private String pincode;

    private CardDetails.AddressType addressType;
}
