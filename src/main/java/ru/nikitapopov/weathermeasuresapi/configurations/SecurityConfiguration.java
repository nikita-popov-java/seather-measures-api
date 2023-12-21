package ru.nikitapopov.weathermeasuresapi.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

    @Autowired
    public SecurityConfiguration(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(HttpMethod.POST, "/sensors").hasAuthority(Authority.ROLE_ADMIN.toString())
                                .requestMatchers(HttpMethod.POST, "/measurements/add").hasAnyAuthority(
                                        Authority.ROLE_EMPLOYEE.toString(),
                                        Authority.ROLE_ADMIN.toString()
                                ).requestMatchers(HttpMethod.GET, "/measurements", "/measurements/**").hasAnyAuthority(
                                        Authority.ROLE_USER.toString(),
                                        Authority.ROLE_EMPLOYEE.toString(),
                                        Authority.ROLE_ADMIN.toString()
                                ).requestMatchers("/auth/**").permitAll()
                ).sessionManagement(config ->
                    config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }
}
