package com.programmingtechie.gatewayservice.config;

import com.programmingtechie.gatewayservice.KeycloakRoleConverter;
import com.programmingtechie.gatewayservice.ReactiveJwtConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public KeycloakRoleConverter keycloakRoleConverter() {
        return new KeycloakRoleConverter();
    }

    @Bean
    public ReactiveJwtAuthenticationConverterAdapter reactiveJwtConverter(KeycloakRoleConverter keycloakRoleConverter) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(keycloakRoleConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveJwtAuthenticationConverterAdapter reactiveJwtConverter) {
        System.out.println("------------------------------------------------------------------------------");
        log.info("------------------------------------------------------------------------------");

        return http.csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/order/**").hasRole("ADMIN")
                        .pathMatchers("/api/product/**").hasAnyRole("ADMIN", "USER", "VIEWER")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jetConfigure -> jetConfigure
                                .jwtAuthenticationConverter(reactiveJwtConverter)
                        )
                )
                .build();
    }
}