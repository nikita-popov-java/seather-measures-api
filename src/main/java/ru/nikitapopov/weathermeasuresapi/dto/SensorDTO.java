package ru.nikitapopov.weathermeasuresapi.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {

    @Size(min = 3, max = 30, message = "Название сенсора должно иметь длину от 3 до 30 символов!")
    private String name;
}
