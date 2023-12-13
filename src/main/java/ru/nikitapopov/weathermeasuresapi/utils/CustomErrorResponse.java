package ru.nikitapopov.weathermeasuresapi.utils;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomErrorResponse {
    private String errors;
}
