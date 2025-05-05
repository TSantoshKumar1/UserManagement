package com.service.demo.securityConfig;

import com.service.demo.jwtConfig.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {


    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf->csrf.disable()).
                cors(cors->cors.disable()).
                addFilterBefore(jwtRequestFilter, AuthorizationFilter.class).
                authorizeHttpRequests(auth-> auth.requestMatchers("/v1/demoapi/add","/v1/demoapi/login").permitAll().
                        requestMatchers("/v1/demoapi/demo").hasAnyRole("USER","ADMIN").
                        anyRequest().authenticated());

        return   http.build();

    }
}
