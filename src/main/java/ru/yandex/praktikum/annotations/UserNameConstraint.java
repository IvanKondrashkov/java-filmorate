package ru.yandex.praktikum.annotations;

import java.lang.annotation.*;
import javax.validation.Payload;
import javax.validation.Constraint;
import ru.yandex.praktikum.validations.UserNameValidator;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameValidator.class)
@Documented
public @interface UserNameConstraint {
    String message() default "Incorrect user name has been entered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
