package ru.nikitapopov.weathermeasuresapi.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.nikitapopov.weathermeasuresapi.models.ApiUser;
import ru.nikitapopov.weathermeasuresapi.services.ApiUserDetailsService;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final ApiUserDetailsService apiUserDetailsService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, ApiUserDetailsService apiUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.apiUserDetailsService = apiUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenHeader = request.getHeader(AUTHORIZATION_HEADER_KEY);

        if (tokenHeader != null && !tokenHeader.isBlank() && tokenHeader.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX)) {

            String jwtToken = tokenHeader.substring(7);

            if (jwtToken.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid authorization token!");
            } else {
                try {
                    String username = jwtUtil.retrieveUsernameClaimFromToken(jwtToken);
                    ApiUserDetails apiUserDetails = (ApiUserDetails) apiUserDetailsService.loadUserByUsername(username);
                    SecurityContext securityContext = SecurityContextHolder.getContext();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            apiUserDetails, apiUserDetails.getPassword(), apiUserDetails.getAuthorities()
                    );

                    if (securityContext.getAuthentication() != null) {
                        securityContext.setAuthentication(null);
                    }

                    securityContext.setAuthentication(authenticationToken);
                } catch (JWTVerificationException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid authorization token!");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
