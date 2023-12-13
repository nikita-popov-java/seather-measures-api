package ru.nikitapopov.weathermeasuresapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sensor")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Sensor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 3, max = 30, message = "Название сенсора должно иметь длину от 3 до 30 символов!")
    @NonNull
    private String name;

    @Column(name = "last_measurement")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime lastMeasurement;

    @OneToMany(mappedBy = "sensor")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private List<Measurement> measurements;
}
