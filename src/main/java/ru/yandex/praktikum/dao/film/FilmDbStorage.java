package ru.yandex.praktikum.dao.film;

import java.util.List;
import java.util.LinkedHashSet;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import ru.yandex.praktikum.dao.genre.GenreStorage;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.Mpa;
import ru.yandex.praktikum.model.Film;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public Film findById(Long id) {
        final String sqlQuery =
                "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, M.MPA_ID, M.MPA_NAME FROM FILMS" +
                        " JOIN MPA M ON M.MPA_ID = FILMS.MPA_ID " +
                        "WHERE FILM_ID = ?";
        if (id < 1) {
            throw new NotFoundException(String.format("Film with id=%d not found!", id));
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public List<Film> findAll() {
        final String sqlQuery =
                "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, M.MPA_ID, M.MPA_NAME FROM FILMS" +
                        " JOIN MPA M ON M.MPA_ID = FILMS.MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public List<Film> findPopularFilms(Integer count) {
        final String sqlQuery =
                "SELECT FILMS.FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, M.MPA_ID, M.MPA_NAME FROM FILMS" +
                        " JOIN MPA M ON M.MPA_ID = FILMS.MPA_ID " +
                        "LEFT JOIN LIKES AS L ON FILMS.FILM_ID = L.FILM_ID" +
                        " GROUP BY FILMS.FILM_ID ORDER BY COUNT(L.USER_ID) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Film save(Film film) {
        final String sqlQuery = "INSERT INTO FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        genreStorage.setFilmGenre(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        final String sqlQuery =
                "UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?" +
                        " WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId()
        );
        genreStorage.setFilmGenre(film);
        return findById(film.getId());
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        final String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        final String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(
                resultSet.getLong("FILM_ID"),
                resultSet.getString("FILM_NAME"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getDate("RELEASE_DATE").toLocalDate(),
                resultSet.getInt("DURATION"),
                new Mpa(
                        resultSet.getLong("MPA_ID"),
                        resultSet.getString("MPA_NAME")
                ),
                new LinkedHashSet<>()
        );
        film.setGenres(genreStorage.loadFilmGenre(film));
        return film;
    }
}
