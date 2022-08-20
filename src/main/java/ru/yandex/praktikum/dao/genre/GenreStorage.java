package ru.yandex.praktikum.dao.genre;

import java.util.List;
import java.util.Set;
import ru.yandex.praktikum.model.Film;
import ru.yandex.praktikum.model.Genre;

public interface GenreStorage {
    Genre findById(Long id);
    List<Genre> findAll();
    Genre save(Genre genre);
    Genre update(Genre genre);
    void deleteById(Long id);
    void setFilmGenre(Film film);
    Set<Genre> loadFilmGenre(Film film);
}
