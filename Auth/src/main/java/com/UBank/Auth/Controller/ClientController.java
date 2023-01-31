package com.UBank.Auth.Controller;

import com.UBank.Auth.Enum.ClientDetails.*;
import com.UBank.Auth.IO.Request.ClientDetailsRequest;
import com.UBank.Auth.Util.ApiResponse;
import com.UBank.Auth.Util.Constants;
import com.UBank.Auth.Service.ClientService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addNewClient(@RequestBody ClientDetailsRequest clientDetailsRequest) {
        Map data = new HashMap();
        data.put(Constants.DATA, clientService.addNewClient(clientDetailsRequest));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @Secured({"ROLE_ADMIN","ROLE_STAFF"})
    @GetMapping("/fetch")
    public ResponseEntity<ApiResponse> fetchClientById(@RequestParam Integer clientId) {
        Map data = new HashMap();
        data.put(Constants.DATA, clientService.fetchClientById(clientId));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @Secured({"ROLE_CLIENT"})
    @GetMapping("/fetchByClient")
    public ResponseEntity<ApiResponse> fetchByClient(HttpServletRequest req) {
        Map data = new HashMap();
        data.put(Constants.DATA, clientService.fetchByClient(req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @GetMapping("/fetchByCrn")
    public ResponseEntity<ApiResponse> fetchByCrn(String crn) {
        Map data = new HashMap();
        data.put(Constants.DATA, clientService.fetchByCrn(crn));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }
}
