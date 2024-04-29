package br.com.don.config.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/colaboradores/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/colaboradores/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/entregadores/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/entregadores/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/freelancers/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/freelancers/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/vales/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/vales/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/tipo-vales").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
