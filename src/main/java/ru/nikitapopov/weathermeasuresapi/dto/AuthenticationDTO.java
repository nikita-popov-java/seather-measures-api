package ru.nikitapopov.weathermeasuresapi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationDTO {
    private String username;
    private String password;
}
