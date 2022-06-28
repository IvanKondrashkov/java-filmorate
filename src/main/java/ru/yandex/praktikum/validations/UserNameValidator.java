package ru.yandex.praktikum.validations;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.annotations.UserNameConstraint;

@Slf4j
public class UserNameValidator implements ConstraintValidator<UserNameConstraint, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        log.info("Validation of the transmitted user name: {}", user);
        if (Objects.isNull(user.getLogin())) {
            return false;
        }
        if (Objects.isNull(user.getName()) || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return true;
    }
}
