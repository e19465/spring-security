package com.sasindu.springsecurity.security.config;


import com.sasindu.springsecurity.constants.ApplicationConstants;
import com.sasindu.springsecurity.security.jwt.JWTAuthEntryPoint;
import com.sasindu.springsecurity.security.jwt.JWTAuthFilter;
import com.sasindu.springsecurity.security.services.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final AppUserDetailsService _userDetailsService;
    private final JWTAuthEntryPoint _jwtAuthEntryPoint;



    /**
     * CookieSameSiteSupplier bean - setting the SameSite attribute to Strict
     *
     * @return CookieSameSiteSupplier object
     */
    @Bean
    public CookieSameSiteSupplier cookieSameSiteSupplier() {
        return CookieSameSiteSupplier.ofStrict();
    }


    /**
     * CorsConfigurationSource bean - setting the CORS configuration
     *
     * @return CorsConfigurationSource object
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(ApplicationConstants.getCorsAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList(ApplicationConstants.getCorsAllowedMethods()));
        configuration.setAllowedHeaders(Arrays.asList(ApplicationConstants.getCorsAllowedHeaders()));
        configuration.setAllowCredentials(ApplicationConstants.isCorsAllowCredentials());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Password encoder bean - use BCryptPasswordEncoder
     *
     * @return PasswordEncoder object
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        try {
            return new BCryptPasswordEncoder();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * AuthTokenFilter bean
     *
     * @return AuthTokenFilter object
     */
    @Bean
    public JWTAuthFilter jWTAuthFilter() {
        try {
            return new JWTAuthFilter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Authentication manager bean
     *
     * @param authConfig the AuthenticationConfiguration object
     * @return AuthenticationManager object
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        try {
            return authConfig.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * DaoAuthenticationProvider bean
     *
     * @return DaoAuthenticationProvider object
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        try {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(_userDetailsService);
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * Security filter chain bean
     *
     * @param httpSecurity the HttpSecurity object
     * @return SecurityFilterChain object
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        try{
            httpSecurity
                    .cors(Customizer.withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session       .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(req ->
                            req.requestMatchers(ApplicationConstants.PUBLIC_URLS).permitAll()
                                    .anyRequest().authenticated()
                    )
                    .exceptionHandling(ex -> ex.authenticationEntryPoint(_jwtAuthEntryPoint))
                    .authenticationProvider(daoAuthenticationProvider())
                    .addFilterBefore(jWTAuthFilter(), UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
