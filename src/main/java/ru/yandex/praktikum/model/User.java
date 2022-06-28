package ru.yandex.praktikum.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import javax.validation.constraints.*;
import ru.yandex.praktikum.annotations.UserNameConstraint;

@Data
@AllArgsConstructor
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
}
