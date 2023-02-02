package com.BankingServices.UBank.Util;

import java.security.SecureRandom;

public class Utils {

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
}
