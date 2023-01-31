package com.UBank.Auth.Security;

import com.UBank.Auth.Model.Authority;
import com.UBank.Auth.Model.Client;
import com.UBank.Auth.Model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;
import java.util.HashSet;

public class UserPrincipal {
    Client client;

    public UserPrincipal(Client client) {
        this.client = client;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new HashSet<>();
        Collection<Authority> authority = new HashSet<>();

        Collection<Role> roles = client.getRoles();

        if (roles == null) return authorities;

        roles.forEach((role) -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authority.addAll(role.getAuthorities());
        });

        authority.forEach((authorityEntityobj) -> {
            authorities.add(new SimpleGrantedAuthority(authorityEntityobj.getName()));
        });

        return authorities;
    }
}
