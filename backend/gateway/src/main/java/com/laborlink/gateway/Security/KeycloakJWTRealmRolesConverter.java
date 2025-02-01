package com.laborlink.gateway.Security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;


public class KeycloakJWTRealmRolesConverter implements Converter<Jwt, Flux<GrantedAuthority>> {


    @Override
    @SuppressWarnings("unchecked")
    public Flux<GrantedAuthority> convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        /*
         * Keycloack has different level of roles client level roles and realm roles realm level
         * We want to acess and load the realm roels from JWT 
         */
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            // Convert each Role to SimpleGrantedAuthorityClass
            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }

        // return a flux from  authorities list to security layer
        return Flux.fromIterable(authorities);
    }
    
}
