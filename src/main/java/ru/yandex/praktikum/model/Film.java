package ru.yandex.praktikum.model;

import lombok.*;
import java.util.Set;
import java.time.LocalDate;
import javax.validation.constraints.*;
import ru.yandex.praktikum.annotations.ReleaseDateConstraint;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    @NotBlank(message = "Incorrect film name has been entered")
    private String name;
    @Size(max = 200, message = "Incorrect film description has been entered")
    private String description;
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive(message = "Incorrect film duration has been entered")
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres;
}
