package ru.nikitapopov.weathermeasuresapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

        if (tokenHeader == null
            || tokenHeader.isBlank()
                || tokenHeader.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX)
                || tokenHeader.substring(7).isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid authorization token!");
        } else {
            String username = jwtUtil.retrieveUsernameClaimFromToken(tokenHeader.substring(7));
            Optional<ApiUser> user = apiUserDetailsService.findUserOptionalByUsername(username);
            ApiUserDetails apiUserDetails = new ApiUserDetails(user.orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден!")));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    apiUserDetails, apiUserDetails.getPassword(), apiUserDetails.getAuthorities()
            );

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
