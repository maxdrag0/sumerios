package com.mad.sumerios.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

@Getter
public enum RolUser {
    ADMINISTRADOR(Set.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))),
    VECINO(Set.of(new SimpleGrantedAuthority("ROLE_VECINO")));

    private final Set<GrantedAuthority> authorities;

    RolUser(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

}
