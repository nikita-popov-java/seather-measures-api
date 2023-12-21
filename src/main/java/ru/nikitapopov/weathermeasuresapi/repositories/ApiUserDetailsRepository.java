package ru.nikitapopov.weathermeasuresapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitapopov.weathermeasuresapi.models.ApiUser;
import ru.nikitapopov.weathermeasuresapi.security.ApiUserDetails;

import java.util.Optional;

@Repository
public interface ApiUserDetailsRepository extends JpaRepository<ApiUser, Integer> {
    Optional<ApiUser> findByUsername(String username);
}
