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
import ru.yandex.praktikum.model.Film;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private AtomicLong idCurrent = new AtomicLong();
    private final Map<Long, Film> films = new LinkedHashMap<>();

    private Long getIdCurrent() {
        return idCurrent.incrementAndGet();
    }

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("Save film in storage: {}", film);
        film.setId(getIdCurrent());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            RuntimeException e = new ValidationException("A film with this id was not found!");
            log.error(e.getMessage());
            throw e;
        }
        films.put(film.getId(), film);
        return film;
    }
}
