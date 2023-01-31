package com.BankingServices.UBank.IO.AuthDto;

import com.BankingServices.UBank.Enum.AccountDetails;
import lombok.Data;

@Data
public class ClientAddressResponse {

    private String houseNumber;

    private String area;

    private String city;

    private String district;

    private String state;

    private String pincode;

    private AccountDetails.AddressType addressType;
}
