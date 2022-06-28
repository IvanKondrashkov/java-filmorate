package ru.yandex.praktikum.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import javax.validation.constraints.*;
import ru.yandex.praktikum.annotations.ReleaseDateConstraint;

@Data
@AllArgsConstructor
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
}
