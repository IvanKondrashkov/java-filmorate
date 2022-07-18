package ru.yandex.praktikum.model;

import lombok.*;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.yandex.praktikum.annotations.ReleaseDateConstraint;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonInclude
    private final Set<Long> likes = new HashSet<>();
}
