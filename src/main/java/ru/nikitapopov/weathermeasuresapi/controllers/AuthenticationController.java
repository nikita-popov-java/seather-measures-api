package ru.nikitapopov.weathermeasuresapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitapopov.weathermeasuresapi.dto.AuthenticationDTO;
import ru.nikitapopov.weathermeasuresapi.models.ApiUser;
import ru.nikitapopov.weathermeasuresapi.security.ApiUserDetails;
import ru.nikitapopov.weathermeasuresapi.security.JwtUtil;
import ru.nikitapopov.weathermeasuresapi.services.ApiUserService;
import ru.nikitapopov.weathermeasuresapi.utils.AuthenticationDTOValidator;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationDTOValidator authenticationDTOValidator;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ApiUserService apiUserService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(
            AuthenticationDTOValidator authenticationDTOValidator,
            JwtUtil jwtUtil, AuthenticationManager authenticationManager, ApiUserService apiUserService, ModelMapper modelMapper
    ) {
        this.authenticationDTOValidator = authenticationDTOValidator;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.apiUserService = apiUserService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authenticationDTO.getUsername(), authenticationDTO.getPassword()
        );

        try {
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException ex) {
            return Collections.singletonMap("error", "Invalid credentials!");
        }

        String jwt = jwtUtil.createToken(authenticationDTO.getUsername());
        return Collections.singletonMap("token", jwt);
    }

    @PostMapping("/registration")
    public Map<?, ?> register(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                   BindingResult bindingResult) {

        authenticationDTOValidator.validate(authenticationDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("error", Objects.requireNonNull(
                    Objects.requireNonNull(
                            bindingResult.getFieldError("username")
                    ).getDefaultMessage()
            ));
        }

        ApiUser user = apiUserService.register(convertToApiUser(authenticationDTO));
        String jwt = jwtUtil.createToken(user.getUsername());

        return Map.of("token", jwt);
    }

    private ApiUser convertToApiUser(AuthenticationDTO authenticationDTO) {
        return modelMapper.map(authenticationDTO, ApiUser.class);
    }
}
