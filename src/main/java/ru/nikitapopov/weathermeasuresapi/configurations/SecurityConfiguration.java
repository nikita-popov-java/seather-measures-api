package ru.nikitapopov.weathermeasuresapi.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nikitapopov.weathermeasuresapi.security.JwtFilter;
import ru.nikitapopov.weathermeasuresapi.services.ApiUserDetailsService;
import ru.nikitapopov.weathermeasuresapi.utils.Authority;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final ApiUserDetailsService apiUserDetailsService;

    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter, ApiUserDetailsService apiUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.apiUserDetailsService = apiUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/sensors/**").hasAuthority(
                                Authority.ROLE_ADMIN.toString()
                        ).requestMatchers(HttpMethod.POST, "/api/v1/measurements/**").hasAnyAuthority(
                                Authority.ROLE_EMPLOYEE.toString(),
                                Authority.ROLE_ADMIN.toString()
                        ).requestMatchers(HttpMethod.GET, "/api/v1/measurements/**").hasAnyAuthority(
                                Authority.ROLE_USER.toString(),
                                Authority.ROLE_EMPLOYEE.toString(),
                                Authority.ROLE_ADMIN.toString()
                        ).anyRequest().permitAll()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(apiUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
