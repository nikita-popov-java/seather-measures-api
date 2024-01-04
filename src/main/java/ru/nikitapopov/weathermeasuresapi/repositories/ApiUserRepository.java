package ru.nikitapopov.weathermeasuresapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitapopov.weathermeasuresapi.models.ApiUser;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Integer> {
    Optional<ApiUser> findByUsername(String username);
}
