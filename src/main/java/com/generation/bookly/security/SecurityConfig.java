package com.generation.bookly.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.generation.bookly.repository.UserRepository;
import com.generation.bookly.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth
	            // 1. Auth & Error (Sempre aperti)
                .requestMatchers("/bookly/api/auth/**", "/error").permitAll()

                // 2. Homepage e Ricerca (GET pubblici)
                // Permettiamo di vedere le attività e i singoli servizi senza essere loggati
                .requestMatchers(HttpMethod.GET, "/bookly/api/activities/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/bookly/api/services/**").permitAll()
                .requestMatchers("/bookly/api/users/**").authenticated()
                //TODO
	            .anyRequest().authenticated()
                //.anyRequest().permitAll()
	        )
	        // Rimuovi .authenticationProvider(authenticationProvider) da qui
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Fondamentale per il token:
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); 
        configuration.setAllowCredentials(true); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(-102); // Priorità massima: deve girare prima di tutto
        return bean;
    }

    @Autowired
    private UserRepository userRepository; // Assicurati di avere il repository degli utenti

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // Supponendo che 'username' nel token sia l'email
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
        };
    }
}