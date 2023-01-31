package com.CardManagement.card.FeignClient;

import com.CardManagement.card.Io.AuthDto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.LinkedHashMap;

@FeignClient(name = "bank", url = "${feign.client.url.domain}" + "/bank")
public interface BankClient {

    @GetMapping(value = "/transaction/handleCreditCardPaymentRequest")
    LinkedHashMap handleCreditCardPayment(@RequestBody TransactionRequest transactionRequest, @RequestHeader("Authorization") String authorization);
}
