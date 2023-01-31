package com.BankingServices.UBank.Controller;

import com.BankingServices.UBank.Enum.AccountDetails;
import com.BankingServices.UBank.IO.Request.TransactionRequest;
import com.BankingServices.UBank.Service.TransactionService;
import com.BankingServices.UBank.Util.ApiResponse;
import com.BankingServices.UBank.Util.Constants;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> makeTransfer(@RequestBody TransactionRequest transactionRequest, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, transactionService.makeTransaction(transactionRequest, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, AccountDetails.CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse> deposit(@RequestBody TransactionRequest transactionRequest, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, transactionService.deposit(transactionRequest, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, AccountDetails.CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/withdrawal")
    public ResponseEntity<ApiResponse> withdrawal(@RequestBody TransactionRequest transactionRequest, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, transactionService.withdrawal(transactionRequest, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, AccountDetails.CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/handleCreditCardPaymentRequest")
    public ResponseEntity<ApiResponse> handleCreditCardPayment(@RequestBody TransactionRequest transactionRequest){
        Map data = new HashMap();
        data.put(Constants.DATA, transactionService.handleCreditCardPayment(transactionRequest));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, AccountDetails.CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/makeCreditCardPayment")
    public ResponseEntity<ApiResponse> makeCreditCardPayment(@RequestBody TransactionRequest transactionRequest,
                                                             @RequestParam String cardNumber, HttpServletRequest req){
        Map data = new HashMap();
        data.put(Constants.DATA, transactionService.makeCreditCardPayment(transactionRequest, cardNumber, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, AccountDetails.CustomHttpStatus.SUCCESS.name()));
    }

}
