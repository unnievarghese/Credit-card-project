package com.UBank.Auth.Security;

import com.UBank.Auth.IO.Request.UserLoginRequest;
import com.UBank.Auth.Model.Client;
import com.UBank.Auth.Service.ClientService;
import com.UBank.Auth.SpringApplicationContext;
import com.UBank.Auth.Util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserLoginRequest creds = new ObjectMapper().
                    readValue(request.getInputStream(), UserLoginRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain, Authentication auth)
            throws IOException, ServletException {

        String userName = ((User) auth.getPrincipal()).getUsername();
        ClientService clientService = (ClientService) SpringApplicationContext.getBean("clientService");
        Client client = clientService.getUser(userName);
        List<String> role = Utils.getAuthorities(client);
        String token = Jwts.builder().setSubject(userName).claim("role",role).claim("CRN",client.getCrn()).
                setExpiration(new Date(System.currentTimeMillis() +SecurityConstants.EXPIRATION_TIME)).
                signWith(SignatureAlgorithm.HS512,SecurityConstants.tokenSecret).compact();

        res.addHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX+token);
        res.addHeader("CRN",client.getCrn());
    }
}
