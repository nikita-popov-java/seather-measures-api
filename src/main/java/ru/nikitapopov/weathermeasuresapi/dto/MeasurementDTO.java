package ru.nikitapopov.weathermeasuresapi.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDTO {

    @Range(min = -100, max = 100, message = "Значение температуры должно быть в диапазоне от -100 до 100")
    private double value;

    private boolean raining;

    private SensorDTO sensor;
}
