package ru.yandex.praktikum.model;

import lombok.*;
import java.time.LocalDate;
import javax.validation.constraints.*;
import ru.yandex.praktikum.annotations.UserNameConstraint;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@UserNameConstraint
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;
    private String name;
    @NotNull
    @NotBlank(message = "Incorrect user login has been entered")
    private String login;
    @Email(message = "Incorrect user email has been entered")
    private String email;
    @Past(message = "Incorrect user birthday has been entered")
    private LocalDate birthday;
}
