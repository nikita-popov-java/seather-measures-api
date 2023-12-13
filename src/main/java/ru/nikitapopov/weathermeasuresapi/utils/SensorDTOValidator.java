package ru.nikitapopov.weathermeasuresapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nikitapopov.weathermeasuresapi.dto.SensorDTO;
import ru.nikitapopov.weathermeasuresapi.models.Sensor;
import ru.nikitapopov.weathermeasuresapi.services.SensorService;

@Component
public class SensorDTOValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public SensorDTOValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Sensor.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SensorDTO sensorDTO = (SensorDTO) target;

        if (sensorService.findByName(sensorDTO.getName()).isPresent()) {
            errors.rejectValue("name", "", "Сенсор с таким именем уже существует!");
        }
    }
}
