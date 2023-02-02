package com.CardManagement.card.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;

@FeignClient(name = "auth", url = "${feign.client.url.domain}" + "/auth")
public interface AuthClient {

    @GetMapping(value = "/client/fetchByClient")
    LinkedHashMap getClientDetails(@RequestHeader("Authorization") String authorization);

    @GetMapping(value = "/client/fetchByCrn")
    LinkedHashMap getClientDetailsByCrn(@RequestHeader("Authorization") String authorization, @RequestParam("crn") String crn);

    @GetMapping(value = "/client/fetch")
    LinkedHashMap getClientById(@RequestHeader("Authorization") String authorization, @RequestParam Integer clientId);
}
