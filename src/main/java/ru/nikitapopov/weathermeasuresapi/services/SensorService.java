package ru.nikitapopov.weathermeasuresapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitapopov.weathermeasuresapi.models.Sensor;
import ru.nikitapopov.weathermeasuresapi.repositories.SensorRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor findByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }
}
