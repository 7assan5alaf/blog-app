package com.hks.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;

    @Bean
     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request->{
                            request.requestMatchers("/auth/**","/home/**")
                                    .permitAll()
                                    .requestMatchers( "/swagger-ui.html", "/v2/api-docs"
                                            , "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
                                            "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/webjars/**").permitAll()
                                    .requestMatchers("/user/**").hasAnyAuthority("USER")
                                    .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                    .anyRequest().authenticated();
                        }
                ).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
