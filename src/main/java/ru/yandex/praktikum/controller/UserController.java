package ru.yandex.praktikum.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicLong;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.exception.ValidationException;
import ru.yandex.praktikum.model.User;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private AtomicLong idCurrent = new AtomicLong();
    private final Map<Long, User> users = new LinkedHashMap<>();

    private Long getIdCurrent() {
        return idCurrent.incrementAndGet();
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        log.info("Save user in storage: {}", user);
        user.setId(getIdCurrent());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            RuntimeException e = new ValidationException("A user with this id was not found!");
            log.error(e.getMessage());
            throw e;
        }
        users.put(user.getId(), user);
        return user;
    }
}
