package ru.nikitapopov.weathermeasuresapi.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitapopov.weathermeasuresapi.dto.AuthenticationDTO;
import ru.nikitapopov.weathermeasuresapi.security.ApiUserDetails;
import ru.nikitapopov.weathermeasuresapi.security.JwtUtil;
import ru.nikitapopov.weathermeasuresapi.utils.AuthenticationDTOValidator;
import ru.nikitapopov.weathermeasuresapi.utils.Authority;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationDTOValidator authenticationDTOValidator;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationDTOValidator authenticationDTOValidator, JwtUtil jwtUtil) {
        this.authenticationDTOValidator = authenticationDTOValidator;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                     BindingResult bindingResult) {
        authenticationDTOValidator.validate(authenticationDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return Collections.singletonMap("error", "Invalid credentials!");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(), authenticationDTO.getPassword()
        );
//        authenticationManager.authenticate(token);

        ApiUserDetails user = (ApiUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String jwt = jwtUtil.createToken(user.getUsername());
        return Collections.singletonMap("token", jwt);
    }
}
