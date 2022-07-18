package ru.yandex.praktikum.dao.film;

import java.util.List;
import ru.yandex.praktikum.model.Film;

public interface FilmStorage {
    Film findById(Long id);
    List<Film> findAll();
    List<Film> findPopularFilms(Integer count);
    Film save(Film film);
    Film update(Film film);
    void addLike(Long id, Long userId);
    void deleteLike(Long id, Long userId);
}
