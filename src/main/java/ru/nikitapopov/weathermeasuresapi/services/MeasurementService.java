package ru.nikitapopov.weathermeasuresapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitapopov.weathermeasuresapi.models.Measurement;
import ru.nikitapopov.weathermeasuresapi.models.Sensor;
import ru.nikitapopov.weathermeasuresapi.repositories.MeasurementRepository;
import ru.nikitapopov.weathermeasuresapi.utils.MeasurementNotSavedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorService sensorService) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
    }

    @Transactional
    public void save(Measurement measurement) {
        String ownerSensorName = measurement.getSensor().getName();
        Optional<Sensor> owner = sensorService.findByName(ownerSensorName);

        if (owner.isEmpty())
            throw new MeasurementNotSavedException("Сенсора с именем '" + ownerSensorName + "' не существует!");

        measurement.setSensor(owner.get());

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
