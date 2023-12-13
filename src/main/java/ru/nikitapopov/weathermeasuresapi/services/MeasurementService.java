package ru.nikitapopov.weathermeasuresapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitapopov.weathermeasuresapi.models.Measurement;
import ru.nikitapopov.weathermeasuresapi.repositories.MeasurementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Transactional
    public void save(Measurement measurement) {
        measurement.getSensor().setLastMeasurement(LocalDateTime.now());
        measurementRepository.save(measurement);
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public int getRainyDaysCount() {
        return measurementRepository.getRainyDaysCount();
    }
}
