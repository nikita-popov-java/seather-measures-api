package ru.nikitapopov.weathermeasuresapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nikitapopov.weathermeasuresapi.repositories.ApiUserDetailsRepository;
import ru.nikitapopov.weathermeasuresapi.security.ApiUserDetails;

@Service
public class ApiUserDetailsService implements UserDetailsService {

    private final ApiUserDetailsRepository apiUserDetailsRepository;

    @Autowired
    public ApiUserDetailsService(ApiUserDetailsRepository apiUserDetailsRepository) {
        this.apiUserDetailsRepository = apiUserDetailsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new ApiUserDetails(
                apiUserDetailsRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Такого пользователя не существует!"))
        );
    }
}
