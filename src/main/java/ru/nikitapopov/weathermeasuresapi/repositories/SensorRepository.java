package ru.nikitapopov.weathermeasuresapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nikitapopov.weathermeasuresapi.models.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    public Sensor findByName(String name);
}
