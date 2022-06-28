package ru.yandex.praktikum.validations;

import java.time.Month;
import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.praktikum.annotations.ReleaseDateConstraint;

@Slf4j
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        log.info("Validation of the transmitted date: {}", date);
        LocalDate constraint = LocalDate.of(1895, Month.DECEMBER, 28);
        return date.isAfter(constraint);
    }
}
