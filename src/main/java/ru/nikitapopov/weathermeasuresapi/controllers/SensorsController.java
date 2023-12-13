package ru.nikitapopov.weathermeasuresapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.nikitapopov.weathermeasuresapi.dto.SensorDTO;
import ru.nikitapopov.weathermeasuresapi.models.Sensor;
import ru.nikitapopov.weathermeasuresapi.services.SensorService;
import ru.nikitapopov.weathermeasuresapi.utils.CustomErrorResponse;
import ru.nikitapopov.weathermeasuresapi.utils.SensorNotRegisterException;
import ru.nikitapopov.weathermeasuresapi.utils.SensorDTOValidator;

@RestController
@RequestMapping("/api/v1/sensors")
public class SensorsController {

    private final SensorService sensorService;
    private final SensorDTOValidator sensorDTOValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorService sensorService, SensorDTOValidator sensorDTOValidator, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.sensorDTOValidator = sensorDTOValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                     BindingResult result) {

        sensorDTOValidator.validate(sensorDTO, result);

        if (result.hasErrors()) {
            StringBuilder builder = new StringBuilder();
            result.getAllErrors().stream().map(e -> (FieldError) e)
                    .forEach(e -> {
                        builder.append(e.getField())
                                .append(" - ")
                                .append(e.getDefaultMessage())
                                .append("; ");
                    });

            throw new SensorNotRegisterException(builder.toString());
        }

        sensorService.save(convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<CustomErrorResponse> handleException(SensorNotRegisterException e) {
        return new ResponseEntity<>(
                new CustomErrorResponse(e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
