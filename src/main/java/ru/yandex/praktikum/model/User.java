package ru.yandex.praktikum.model;

import lombok.*;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import ru.yandex.praktikum.annotations.UserNameConstraint;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@UserNameConstraint
public class User {
    private Long id;
    @Email(message = "Incorrect user email has been entered")
    private String email;
    @NotNull
    @NotBlank(message = "Incorrect user login has been entered")
    private String login;
    private String name;
    @Past(message = "Incorrect user birthday has been entered")
    private LocalDate birthday;
    @JsonInclude
    private final Set<Long> friends = new HashSet<>();
}
