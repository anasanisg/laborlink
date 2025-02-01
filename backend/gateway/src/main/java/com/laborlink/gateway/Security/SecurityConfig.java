package com.laborlink.gateway.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // Define our custom keycloack jwt converter
        ReactiveJwtAuthenticationConverter jwtAuthConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(new KeycloakJWTRealmRolesConverter());

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable()) // to avoid gateway to send a form instead bearer token

                /*
                 * Security Configurations
                 */
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // LABOR USER
                        .pathMatchers(HttpMethod.POST, "/obtain").hasRole("labor-user")
                        .pathMatchers(HttpMethod.GET, "/tool/*").hasRole("labor-user")
                        .pathMatchers(HttpMethod.GET, "/invoice").hasRole("labor-user")
                        .pathMatchers(HttpMethod.GET, "/invoice/get/*").hasRole("labor-user")

                        // LABOR MANAGER
                        .pathMatchers(HttpMethod.POST, "/tool").hasRole("labor-manager")
                        .pathMatchers(HttpMethod.PUT, "/tool/*").hasRole("labor-manager")

                        // ONLY DEVELOPERS
                        .pathMatchers(HttpMethod.POST, "/tool/get-group-of-tools").hasRole("labor-developer")
                        .pathMatchers(HttpMethod.POST, "/tool/update-group-of-tools").hasRole("labor-developer")

                        .anyExchange().authenticated())

                // Token authorization from oauth2 resource server through issuer url point
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));

        return http.build();
    }
}