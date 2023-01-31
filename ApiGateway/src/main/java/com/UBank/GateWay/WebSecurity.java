package com.UBank.GateWay;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST,SecurityConstant.SIGN_IN_URL).
                permitAll()
                .antMatchers(HttpMethod.POST,SecurityConstant.ADD_CLIENT_URL).
                permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstant.GET_PAYMENT_URL).hasAnyAuthority("ROLE_ADMIN","ROLE_STAFF")
                .antMatchers(HttpMethod.GET, SecurityConstant.GENERATE_URL_URL).hasAnyAuthority("ROLE_ADMIN","ROLE_STAFF")
                .anyRequest().authenticated().
                and().addFilter(new JwtAutheticationFilter(authenticationManager())).
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                exceptionHandling();
    }
}
