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
import ru.nikitapopov.weathermeasuresapi.repositories.ApiUserDetailsRepository;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final ApiUserDetailsRepository apiUserDetailsRepository;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, ApiUserDetailsRepository apiUserDetailsRepository) {
        this.jwtUtil = jwtUtil;
        this.apiUserDetailsRepository = apiUserDetailsRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(AUTHORIZATION_HEADER_KEY);

        if (token == null
            || token.isBlank()
                || token.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX)
                || token.substring(7).isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid authorization token!");
        } else {
            String username = jwtUtil.retrieveUsernameClaimFromToken(token);
            Optional<ApiUser> user = apiUserDetailsRepository.findByUsername(username);
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
