package ru.yandex.praktikum.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.dao.genre.GenreStorage;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.Film;
import ru.yandex.praktikum.model.Genre;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre findById(Long id) {
        final Genre genre = genreStorage.findById(id);

        if (Objects.isNull(genre)) {
            throw new NotFoundException(String.format("Genre with id=%d not found!", id));
        }
        return genre;
    }

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre save(Genre genre) {
        return genreStorage.save(genre);
    }

    public Genre update(Genre genre) {
        genre = genreStorage.update(genre);

        if (Objects.isNull(genre)) {
            throw new NotFoundException("Genre with not found!");
        }
        return genre;
    }


    public void deleteById(Long id) {
        final Genre genre = genreStorage.findById(id);

        if (Objects.isNull(genre)) {
            throw new NotFoundException(String.format("Genre with id=%d not found!", id));
        }
        genreStorage.deleteById(id);
    }

    public void setFilmGenre(Film film) {
        genreStorage.setFilmGenre(film);
    }

    public void loadFilmGenre(Film film) {
        genreStorage.loadFilmGenre(film);
    }
}
