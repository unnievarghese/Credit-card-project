package com.UBank.Auth.Controller;

import com.UBank.Auth.IO.Request.UserLoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwaggerAuthController {

    @PostMapping("/signin")
    public void theFakelogin(@RequestBody UserLoginRequest userLoginRequest){
        throw new IllegalStateException("This method should not be called.This method is implemented by spring security.");
    }
}
