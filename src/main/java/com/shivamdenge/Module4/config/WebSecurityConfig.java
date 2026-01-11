package com.shivamdenge.Module4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
         httpSecurity.authorizeHttpRequests(
                        auth -> auth.requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated())
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig
                        -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

         return httpSecurity.build();
    }
}
