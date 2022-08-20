package ru.yandex.praktikum.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.Film;
import ru.yandex.praktikum.service.FilmService;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) {
        log.info("Send get request /films/{}", id);
        return filmService.findById(id);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Send get request /films");
        return filmService.findAll();
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Send get request /films/popular?count={}", count);
        return filmService.findPopularFilms(count);
    }

    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("Send post request /films {}", film);
        return filmService.save(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Send put request /films {}", film);
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Send delete request /films/{}", id);
        filmService.deleteById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Send put request /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Send delete request /films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
    }
}
