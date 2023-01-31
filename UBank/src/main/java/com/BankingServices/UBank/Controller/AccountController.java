package com.BankingServices.UBank.Controller;

import com.BankingServices.UBank.Enum.AccountDetails.*;
import com.BankingServices.UBank.Service.AccountService;
import com.BankingServices.UBank.Util.*;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAccount(HttpServletRequest req){
        Map data = new HashMap();
        data.put(Constants.DATA, accountService.createAccount(req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse> checkBalance(@RequestParam String accountNumber, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, accountService.checkBalance(accountNumber, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }
}