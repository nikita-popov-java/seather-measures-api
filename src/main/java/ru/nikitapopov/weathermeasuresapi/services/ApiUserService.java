package ru.nikitapopov.weathermeasuresapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nikitapopov.weathermeasuresapi.models.ApiUser;
import ru.nikitapopov.weathermeasuresapi.repositories.ApiUserRepository;
import ru.nikitapopov.weathermeasuresapi.utils.Authority;


@Service
public class ApiUserService {

    private final ApiUserRepository apiUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiUserService(ApiUserRepository apiUserRepository, PasswordEncoder passwordEncoder) {
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiUser register(ApiUser user) {
        user.setRole(Authority.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return apiUserRepository.save(user);
    }
}
