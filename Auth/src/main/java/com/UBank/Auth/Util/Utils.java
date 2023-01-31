package com.UBank.Auth.Util;

import com.UBank.Auth.Model.Authority;
import com.UBank.Auth.Model.Client;
import com.UBank.Auth.Model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class Utils {

    public static List<String> getAuthorities(Client client){

        List<String> authorities = new ArrayList<>();
        Collection<Authority> authorityEntity = new HashSet<>();

        Collection<Role> roles = client.getRoles();

        if (roles == null) return authorities;

        roles.forEach((role) -> {
            authorities.add(role.getName());
            authorityEntity.addAll(role.getAuthorities());
        });

        authorityEntity.forEach((authorityEntityobj) -> {
            authorities.add(authorityEntityobj.getName());
        });

        return authorities;
    }

    public static String generateRandomString(int lenght) {

        SecureRandom RANDOM = new SecureRandom();
        StringBuilder returnValue = new StringBuilder(lenght);
        for (int i = 0; i < lenght; i++) {
            String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static String getCrn (String token){
        token = token.replace("Bearer ", "");
        Claims claims = Jwts.parser()
                .setSigningKey("jf9i4jgu83nfl0")
                .parseClaimsJws(token).getBody();
        return (String) claims.get("CRN");
    }
}
