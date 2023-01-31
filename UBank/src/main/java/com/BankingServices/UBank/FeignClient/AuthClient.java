package com.BankingServices.UBank.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.LinkedHashMap;

@FeignClient(name = "auth", url = "${feign.client.url.domain}" + "/auth")
public interface AuthClient {

    @GetMapping(value = "/client/fetchByClient")
    LinkedHashMap getClientDetails(@RequestHeader("Authorization") String authorization);
}
