package com.UBank.Auth.IO.Request;

import com.UBank.Auth.Enum.ClientDetails;
import lombok.Data;

@Data
public class ClientAddressRequest {

    private String houseNumber;

    private String area;

    private String city;

    private String district;

    private String state;

    private String pincode;

    private ClientDetails.AddressType addressType;
}
