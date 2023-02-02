package com.CardManagement.card.Controller;

import com.CardManagement.card.Io.Request.CardRequest;
import com.CardManagement.card.Io.Request.PurchaseRequest;
import com.CardManagement.card.Io.AuthDto.TransactionRequest;
import com.CardManagement.card.Io.Request.UpdateRequest;
import com.CardManagement.card.Service.CardService;
import com.CardManagement.card.Utils.ApiResponse;
import com.CardManagement.card.Utils.Constants;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.CardManagement.card.Enum.CardDetails.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/activateCard")
    public ResponseEntity<ApiResponse> activateCard(@RequestBody CardRequest cardRequest, HttpServletRequest req) {
        Map data = new HashMap();
        data.put(Constants.DATA, cardService.activateCard(cardRequest, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PatchMapping("/updateCard")
    public ResponseEntity<ApiResponse> updateCardDetails(@RequestBody UpdateRequest updateRequest, @RequestParam String cardNumber, HttpServletRequest req, @RequestParam Integer clientId) throws IllegalAccessException, NoSuchFieldException {
        Map data = new HashMap();
        data.put(Constants.DATA, cardService.updateCardDetails(updateRequest, req, cardNumber, clientId));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/deactivateCard")
    public ResponseEntity<ApiResponse> deactivateCard(@RequestParam String cardNumber, HttpServletRequest req, @RequestParam Integer clientId) throws IllegalAccessException, NoSuchFieldException {
        Map data = new HashMap();
        data.put(Constants.DATA, cardService.deactivateCard(cardNumber, clientId, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/makePurchase")
    public ResponseEntity<ApiResponse> makePurchase(@RequestBody PurchaseRequest purchaseRequest, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, cardService.makePurchase(purchaseRequest, req));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/getPayment")
    public ResponseEntity<ApiResponse> getPayment(@RequestBody TransactionRequest transactionRequest,
                                                   @RequestParam String cardNumber, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, cardService.getPayment(transactionRequest, req, cardNumber));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @PostMapping("/receivePayment")
    public ResponseEntity<ApiResponse> receivePayment(@RequestBody TransactionRequest transactionRequest,
                                                    @RequestParam String cardNumber, HttpServletRequest req) throws Exception {
        Map data = new HashMap();
        data.put(Constants.DATA, cardService.receivePayment(transactionRequest, req, cardNumber));
        return ResponseEntity.ok(new ApiResponse(data, Constants.SUCCESS, CustomHttpStatus.SUCCESS.name()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataType = "string", paramType = "header")
    @GetMapping(value = "/generateBill", produces = "application/zip")
    public ResponseEntity<byte[]> generateBill() throws Exception {
        List<byte[]> out = cardService.generateBill();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "ZipFileName.zip");
        return new ResponseEntity<>(cardService.convertToZip(out), headers, HttpStatus.OK);
    }
}