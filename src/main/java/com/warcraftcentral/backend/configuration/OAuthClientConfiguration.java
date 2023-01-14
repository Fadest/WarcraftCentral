package com.warcraftcentral.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuthClientConfiguration {

    @Bean
    ClientRegistration blizzardClientRegistration(
        @Value("${spring.security.oauth2.client.provider.blizzard.token-uri}") String token_Uri,
        @Value("${spring.security.oauth2.client.registration.blizzard.client-id}") String client_Id,
        @Value("${spring.security.oauth2.client.registration.blizzard.client-secret}") String client_Secret,
        @Value("${spring.security.oauth2.client.registration.blizzard.authorization-grant-type}") String authorizationGrantType
    ) {
        return ClientRegistration
            .withRegistrationId("blizzard")
            .tokenUri(token_Uri)
            .clientId(client_Id)
            .clientSecret(client_Secret)
            .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
            .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(ClientRegistration clientRegistration) {
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }

    @Bean
    public OAuth2AuthorizedClientService auth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
            new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public SecurityFilterChain configuration(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .httpBasic()
            .and()
            .authorizeHttpRequests()
            .anyRequest().permitAll()
            .and()
            .build();
    }

}