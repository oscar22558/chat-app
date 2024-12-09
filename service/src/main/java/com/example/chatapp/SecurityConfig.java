package com.example.chatapp;

import com.example.chatapp.db.entity.RoleType;
import com.example.chatapp.db.repo.AppUserJpaRepo;
import com.example.chatapp.features.user.JWTAuthFilter;
import com.example.chatapp.features.user.UserIdentityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JWTAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> header.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin
                ))
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST,"/api/auth")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.OPTIONS,"/api/auth")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.POST, "/api/user")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.OPTIONS, "/api/user")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.OPTIONS, "/api/**")).permitAll()
                        .requestMatchers(antMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(antMatcher("/chat/**")).permitAll()
                        .requestMatchers(antMatcher("/api/**")).authenticated()
                        .requestMatchers(antMatcher("/api/group/**")).hasRole("USER")
                        .requestMatchers(antMatcher("/api/contact")).hasRole("USER")
                        .requestMatchers(antMatcher("/api/auth/user")).hasRole("USER")
                        .requestMatchers(antMatcher("/api/user")).hasRole("USER")
                        .requestMatchers(antMatcher("/api/user")).hasRole("USER")
                        .anyRequest().denyAll()
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(AppUserJpaRepo appUserJpaRepo){
        return new UserIdentityService(appUserJpaRepo);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager myAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider::authenticate;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS");
            }
        };
    }
}