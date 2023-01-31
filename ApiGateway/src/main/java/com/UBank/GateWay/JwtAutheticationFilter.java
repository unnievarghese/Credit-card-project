package com.UBank.GateWay;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAutheticationFilter extends BasicAuthenticationFilter {
    public JwtAutheticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null)
            token = token.replace("Bearer ", "");
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey("jf9i4jgu83nfl0")
                    .parseClaimsJws(token).getBody();
            String userName = claims.getSubject();
            List<String> authoritiesList = (List<String >) claims.get("role");
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String auth:authoritiesList){
                authorities.add(new SimpleGrantedAuthority(auth));
            }
            if(userName != null){
               UsernamePasswordAuthenticationToken auth =
                       new UsernamePasswordAuthenticationToken(userName,null,authorities);
               SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        catch (Exception ignore){
            SecurityContextHolder.clearContext();
        }
       filterChain.doFilter(request,response);
    }
}
