/*
package com.showcase.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    */
/**
     * The security filter chain allows all requests with an "Authorization" header to pass through since they already
     * have an access-token. If the header is not present, this means the user is not authenticated and thus will be
     * forwarded to the login page
     *
     * @param http
     * @return
     *//*

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(authorize -> authorize.matchers(authorizationHeaderMatcher()).permitAll()
                .anyExchange().authenticated());
        http.oauth2Login(Customizer.withDefaults());
        return http.build();
    }

    */
/**
     * this is a custom matcher that is used in the securityfilterchain to match (and permitAll) requests that contain
     * an "Authorization" header.
     *
     * @return
     *//*

    private ServerWebExchangeMatcher authorizationHeaderMatcher() {
        return (exchange) -> exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)
                ? ServerWebExchangeMatcher.MatchResult.match()
                : ServerWebExchangeMatcher.MatchResult.notMatch();

    }

*/
/*    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {

        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .clientCredentials()
                        .password()
                        .build();

        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new DefaultReactiveOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }*//*


}
*/
