package com.UBank.GateWay;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Utils {

        public static String getCrn (String token){
        Claims claims = Jwts.parser()
                .setSigningKey("jf9i4jgu83nfl0")
                .parseClaimsJws(token).getBody();
        return (String) claims.get("CRN");
    }
}
