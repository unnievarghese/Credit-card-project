package com.BankingServices.UBank.FeignClient;

import com.BankingServices.UBank.IO.Request.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.LinkedHashMap;

@FeignClient(name = "credit", url = "${feign.client.url.domain}" + "/credit")
public interface CardClient {

    @GetMapping(value = "/card/receivePayment")
    LinkedHashMap makeCreditCardPayment(@RequestBody TransactionRequest transactionRequest, @RequestParam String cardNumber, @RequestHeader("Authorization") String authorization);
}
