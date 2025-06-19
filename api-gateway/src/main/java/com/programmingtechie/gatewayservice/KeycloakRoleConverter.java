package com.programmingtechie.gatewayservice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        try {
            System.out.println(">>> Entered KeycloakRoleConverter");
            log.info(">>> Entered KeycloakRoleConverter");

            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            if (realmAccess == null) {
                log.warn("realm_access is null");
                return List.of();
            }

            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null) {
                log.warn("No roles found in realm_access");
                return List.of();
            }

            return roles.stream()
                    .map(role -> "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .peek(auth -> log.info("Granted authority: {}", auth))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Exception in KeycloakRoleConverter", e);
            return List.of();
        }
    }
}