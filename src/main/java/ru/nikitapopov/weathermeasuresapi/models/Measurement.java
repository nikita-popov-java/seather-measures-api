package ru.nikitapopov.weathermeasuresapi.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "measurement")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class Measurement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    @Range(min = -100, max = 100, message = "Значение температуры должно быть в диапазоне от -100 до 100")
    @NonNull
    private double value;

    @Column(name = "raining")
    @NonNull
    private boolean raining;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @NonNull
    private Sensor sensor;
}
