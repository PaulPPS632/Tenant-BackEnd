package com.superfact.inventory.config;

import com.superfact.inventory.Tenant.TenantFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final WebClient userInfoClient;
    private final TenantFilter tenantFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers("/auth/**", "/public/**").permitAll()
                        .requestMatchers("/inventory/**","/user/**").permitAll()
                .anyRequest().authenticated())
                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults()))
                .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();

    }
    @Bean
    public OpaqueTokenIntrospector instrospector(){
        return new GoogleOpaqueTokenIntrospector(userInfoClient);
    }
}
