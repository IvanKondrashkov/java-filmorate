package ru.yandex.praktikum.dao.film;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.yandex.praktikum.model.Film;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private AtomicLong idCurrent = new AtomicLong();
    private final Map<Long, Film> films = new LinkedHashMap<>();

    private Long getIdCurrent() {
        return idCurrent.incrementAndGet();
    }

    public Film findById(Long id) {
        return films.get(id);
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public List<Film> findPopularFilms(Integer count) {
        return findAll().stream()
                .sorted(Comparator.comparing(Film::getLikes, (Comparator.comparingInt(Set::size))).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film save(Film film) {
        film.setId(getIdCurrent());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            return null;
        }
        films.put(film.getId(), film);
        return film;
    }

    public void addLike(Long id, Long userId) {
        films.get(id).getLikes().add(userId);
    }

    public void deleteLike(Long id, Long userId) {
        films.get(id).getLikes().remove(userId);
    }
}
