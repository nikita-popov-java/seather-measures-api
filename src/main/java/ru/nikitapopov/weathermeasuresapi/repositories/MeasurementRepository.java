package ru.nikitapopov.weathermeasuresapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nikitapopov.weathermeasuresapi.models.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    @Query(value = "SELECT COUNT(*) FROM measurement WHERE raining = true", nativeQuery = true)
    public int getRainyDaysCount();
}
