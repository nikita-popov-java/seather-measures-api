package ru.nikitapopov.weathermeasuresapi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.nikitapopov.weathermeasuresapi.dto.MeasurementDTO;
import ru.nikitapopov.weathermeasuresapi.models.Measurement;
import ru.nikitapopov.weathermeasuresapi.models.Sensor;
import ru.nikitapopov.weathermeasuresapi.services.MeasurementService;
import ru.nikitapopov.weathermeasuresapi.services.SensorService;
import ru.nikitapopov.weathermeasuresapi.utils.CustomErrorResponse;
import ru.nikitapopov.weathermeasuresapi.utils.MeasurementNotSavedException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/measurements")
public class MeasurementsController {

    private final MeasurementService measurementService;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementService measurementService, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MeasurementDTO> index() {
        return measurementService.findAll().stream()
                .map(this::convertToMeasurementDTO)
                .toList();
    }

    @GetMapping("/rainyDaysCount")
    public int getRainyDaysCount() {
        return measurementService.getRainyDaysCount();
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult result) {

        if (result.hasErrors()) {

            StringBuilder error = new StringBuilder();

            result.getAllErrors().stream().map(e -> (FieldError) e)
                    .forEach(e -> {
                        error.append(e.getField())
                                .append(" - ")
                                .append(e.getDefaultMessage())
                                .append("; ");
                    });

            throw new MeasurementNotSavedException(error.toString());
        }

        measurementService.save(convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<CustomErrorResponse> handleException(MeasurementNotSavedException e) {
        return new ResponseEntity<>(
                new CustomErrorResponse(e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        String ownerSensorName = measurementDTO.getSensor().getName();
        Optional<Sensor> owner = sensorService.findByName(ownerSensorName);

        if (owner.isEmpty())
            throw new MeasurementNotSavedException("Сенсора с именем '" + ownerSensorName + "' не существует!");

        measurement.setSensor(owner.get());

        return measurement;
    }
}
