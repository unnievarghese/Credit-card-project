package com.CardManagement.card.Utils;

import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;
import java.time.LocalDate;

public class Utils {

    @Value("${billing.day}")
    private Integer billingDate;

    @Value("${grace.period}")
    private Integer gracePeriod;

    @Value("${annual.percentage.rate}")
    private Float apr;

    @Value("${minimum.payment.percent}")
    private Float mpp;

    @Value("${credit.limit}")
    private Float creditLimit;

    @Value("${password}")
    private String password;

    @Value("${email}")
    private String email;

    @Value("${token}")
    private String token;


    public LocalDate billingDate(){
        return LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), billingDate);
    }

    public Integer gracePeriod(){
        return gracePeriod;
    }

    public Integer billingDay(){
        return billingDate;
    }

    public Float getApr(){
        return apr;
    }

    public Float getMpp(){
        return mpp;
    }

    public Float getCreditLimit(){
        return creditLimit;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getToken(){
        return token;
    }

    public String generateNumber(int lenght) {
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder returnValue = new StringBuilder(lenght);
        for (int i = 0; i < lenght; i++) {
            String NUMBERS = "0123456789";
            returnValue.append(NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())));
        }
        return new String(returnValue);
    }

    public String generateRandomString(int lenght) {

        SecureRandom RANDOM = new SecureRandom();
        StringBuilder returnValue = new StringBuilder(lenght);
        for (int i = 0; i < lenght; i++) {
            String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public LocalDate getExpiry() {
        return LocalDate.now().plusYears(5);
    }
}
