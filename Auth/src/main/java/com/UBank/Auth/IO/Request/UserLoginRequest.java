package com.UBank.Auth.IO.Request;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String email;
    private String password;

}
