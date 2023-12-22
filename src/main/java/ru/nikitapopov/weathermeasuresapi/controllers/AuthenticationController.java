package ru.nikitapopov.weathermeasuresapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
import ru.nikitapopov.weathermeasuresapi.services.ApiUserDetailsService;
import ru.nikitapopov.weathermeasuresapi.utils.AuthenticationDTOValidator;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationDTOValidator authenticationDTOValidator;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ApiUserDetailsService apiUserDetailsService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(
            AuthenticationDTOValidator authenticationDTOValidator,
            JwtUtil jwtUtil, AuthenticationManager authenticationManager,
            ApiUserDetailsService apiUserDetailsService, ModelMapper modelMapper
    ) {
        this.authenticationDTOValidator = authenticationDTOValidator;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.apiUserDetailsService = apiUserDetailsService;
        this.modelMapper = modelMapper;
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
        authenticationManager.authenticate(token);

        ApiUserDetails user = (ApiUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String jwt = jwtUtil.createToken(user.getUsername());
        return Collections.singletonMap("token", jwt);
    }

    @PostMapping("/registration")
    public Map<String, String> register(@RequestBody @Valid AuthenticationDTO authenticationDTO,
                                     BindingResult bindingResult) {

        authenticationDTOValidator.validate(authenticationDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return Collections.singletonMap("error", "Invalid credentials!");
        }

        ApiUser user = apiUserDetailsService.save(convertToApiUser(authenticationDTO));

        String jwt = jwtUtil.createToken(user.getUsername());

        System.out.println(user);
        System.out.println(jwt);

        return Collections.singletonMap("token", jwt);
    }

    private ApiUser convertToApiUser(AuthenticationDTO authenticationDTO) {
        return modelMapper.map(authenticationDTO, ApiUser.class);
    }
}
