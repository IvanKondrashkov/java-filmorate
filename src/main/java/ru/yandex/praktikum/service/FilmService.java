package ru.yandex.praktikum.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.dao.film.FilmStorage;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.Film;
import ru.yandex.praktikum.model.User;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film findById(Long id) {
        final Film film = filmStorage.findById(id);

        if (Objects.isNull(film)) {
            throw new NotFoundException(String.format("Film with id=%d not found!", id));
        }
        return film;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.findPopularFilms(count);
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        film = filmStorage.update(film);

        if (Objects.isNull(film)) {
            throw new NotFoundException("Film with not found!");
        }
        return film;
    }

    public void deleteById(Long id) {
        final Film film = filmStorage.findById(id);

        if (Objects.isNull(film)) {
            throw new NotFoundException(String.format("Film with id=%d not found!", id));
        }
        filmStorage.deleteById(id);
    }

    public void addLike(Long id, Long userId) {
        final Film film = filmStorage.findById(id);
        final User user = userService.findById(userId);

        if (Objects.isNull(film)) {
            throw new NotFoundException(String.format("Film with id=%d not found!", id));
        }
        if (Objects.isNull(user)) {
            throw new NotFoundException(String.format("User with id=%d not found!", userId));
        }
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(Long id, Long userId) {
        final Film film = filmStorage.findById(id);
        final User user = userService.findById(userId);

        if (Objects.isNull(film)) {
            throw new NotFoundException(String.format("Film with id=%d not found!", id));
        }
        if (Objects.isNull(user)) {
            throw new NotFoundException(String.format("User with id=%d not found!", userId));
        }
        filmStorage.deleteLike(id, userId);
    }
}
