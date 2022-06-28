package ru.yandex.praktikum.annotations;

import java.lang.annotation.*;
import javax.validation.Payload;
import javax.validation.Constraint;
import ru.yandex.praktikum.validations.ReleaseDateValidator;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
@Documented
public @interface ReleaseDateConstraint {
    String message() default "Incorrect film release date has been entered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
