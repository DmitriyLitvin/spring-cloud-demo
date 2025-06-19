package com.programmingtechie.gatewayservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;

@Slf4j
public class ReactiveJwtConverter extends ReactiveJwtAuthenticationConverterAdapter {

    public ReactiveJwtConverter() {
        super(jwtAuthenticationConverter());
        log.info(">>> ReactiveJwtConverter created");
    }

    private static JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}