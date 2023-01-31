package com.CardManagement.card.CustomPinAuthentication;

import com.CardManagement.card.Io.Request.PurchaseRequest;
import com.CardManagement.card.Model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomPinAuthentication {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public Boolean authenticatePin(PurchaseRequest purchaseRequest, Card card){
        Boolean status = false;
        if(bCryptPasswordEncoder.matches(purchaseRequest.getPin(), card.getEncryptedPin()))
            return true;
        return status;
    }
}
