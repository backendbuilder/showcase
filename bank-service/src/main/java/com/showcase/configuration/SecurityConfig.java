package com.showcase.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((req) -> req
                        .anyRequest().authenticated());
                //.anyRequest().hasAnyAuthority("SCOPE_message.read")
        http.oauth2ResourceServer(t -> t.jwt(Customizer.withDefaults()));
        //http.oauth2ResourceServer(t-> t.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        //http.oauth2Login(Customizer.withDefaults());

        return http.build();




/*        http.authorizeExchange(authorize -> authorize
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("bank-service/actuator/**").permitAll()
                .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .pathMatchers("/login/oath2/**").permitAll()
                .anyExchange().authenticated());
        http.oauth2ResourceServer(t-> t.jwt(Customizer.withDefaults()));
        http.oauth2Login(Customizer.withDefaults());


        http.mvcMatcher("/**")
                .authorizeRequests()
                .mvcMatchers("/**")
                .access("hasAuthority('SCOPE_message.read')")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();*/
    }
}
