package com.rafiqgasmi.techincal.assessment.fileservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private IPAuthenticationFilter ipAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure CSRF protection to ignore all requests
        http.csrf(CsrfConfigurer::disable);
        http.headers(HeadersConfigurer::disable);
        http.addFilterBefore(ipAuthenticationFilter, WebAsyncManagerIntegrationFilter.class);
        return http.build();
    }
}