package ru.nikitapopov.weathermeasuresapi.models;

import jakarta.persistence.*;
import lombok.*;
import ru.nikitapopov.weathermeasuresapi.utils.Authority;

@Entity
@Table(name = "api_user")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ApiUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    @NonNull
    private String username;

    @Column(name = "password")
    @NonNull
    private String password;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    @NonNull
    private Authority role;
}
