package ru.yandex.praktikum.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.Genre;
import ru.yandex.praktikum.service.GenreService;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre findById(@PathVariable Long id) {
        log.info("Send get request /genres/{}", id);
        return genreService.findById(id);
    }

    @GetMapping
    public List<Genre> findAll() {
        log.info("Send get request /genres");
        return genreService.findAll();
    }

    @PostMapping
    public Genre save(@Valid @RequestBody Genre genre) {
        log.info("Send post request /genres {}", genre);
        return genreService.save(genre);
    }

    @PutMapping
    public Genre update(@Valid @RequestBody Genre genre) {
        log.info("Send put request /genres {}", genre);
        return genreService.update(genre);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Send delete request /genres/{}", id);
        genreService.deleteById(id);
    }
}
