package ru.nikitapopov.weathermeasuresapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nikitapopov.weathermeasuresapi.dto.AuthenticationDTO;
import ru.nikitapopov.weathermeasuresapi.repositories.ApiUserDetailsRepository;

@Component
public class AuthenticationDTOValidator implements Validator {

    private final ApiUserDetailsRepository apiUserDetailsRepository;

    @Autowired
    public AuthenticationDTOValidator(ApiUserDetailsRepository apiUserDetailsRepository) {
        this.apiUserDetailsRepository = apiUserDetailsRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(AuthenticationDTO.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuthenticationDTO authenticationDTO = (AuthenticationDTO) target;

        if (apiUserDetailsRepository.findByUsername(authenticationDTO.getUsername()).isEmpty()) {
            errors.rejectValue("username", "", "Пользователя с таким именем не существует!");
        }
    }
}
