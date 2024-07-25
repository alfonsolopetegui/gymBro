package com.myCompany.gymBro.web.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.api.client")
@Getter
@Setter
@NoArgsConstructor
public class GoogleApiProperties {

    private String id;
    private String secret;
    private String redirectUri;
    private String authorizationEndpoint;
    private String tokenEndpoint;
}
