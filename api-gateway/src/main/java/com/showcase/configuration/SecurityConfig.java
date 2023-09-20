package com.showcase.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        http.authorizeExchange(authorize -> authorize
                .pathMatchers(HttpMethod.GET, "/restaurant/public/list").permitAll()
                .pathMatchers(HttpMethod.GET, "/restaurant/public/menu/*").permitAll()
                .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyExchange().authenticated());
        http.oauth2ResourceServer(t-> t.jwt(Customizer.withDefaults()));
        http.oauth2Login(Customizer.withDefaults());

        return http.build();
    }

}