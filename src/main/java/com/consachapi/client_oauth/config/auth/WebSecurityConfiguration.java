package com.consachapi.client_oauth.config.auth;

import com.consachapi.client_oauth.config.ParamsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Value("${service.auth.client-id}")
    private String clientId;

    @Value("${service.auth.issuer-url}")
    private String issuerUrl;

    @Autowired private CustomAuthenticationEntryPoint entryPoint;
    @Autowired private AccessDeniedHandler accessDenied;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        DelegatingJwtGrantedAuthoritiesConverter authoritiesConverter = new DelegatingJwtGrantedAuthoritiesConverter(
                new JwtGrantedAuthoritiesConverter(),
                new JwtAuthConverter(clientId)
        );

        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/authorize/iniciar").permitAll()
                        .requestMatchers("/inicio/admin").hasRole(ParamsManager.ROLE_ADMIN)
                        .requestMatchers("/inicio/user").hasRole(ParamsManager.ROLE_USER)
                        .anyRequest().authenticated()
                )
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(entryPoint))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDenied))
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer( oauth2 -> oauth2.jwt(
                        jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwt -> {
                                    return new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt));
                                }
                            )
                        )
                );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUrl);
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

}
