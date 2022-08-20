package ru.yandex.praktikum.dao.genre;

import java.util.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.Film;
import ru.yandex.praktikum.model.Genre;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(Long id) {
        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM GENRES WHERE GENRE_ID = ?";
        if (id < 1) {
            throw new NotFoundException(String.format("Genre with id=%d not found!", id));
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> findAll() {
        final String sqlQuery = "SELECT GENRE_ID, GENRE_NAME FROM GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre save(Genre genre) {
        final String sqlQuery = "INSERT INTO GENRES(GENRE_NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"GENRE_ID"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        final String sqlQuery = "UPDATE GENRES SET GENRE_NAME = ? WHERE GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery
                , genre.getName()
                , genre.getId());
        return findById(genre.getId());
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "DELETE FROM GENRES WHERE GENRE_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void setFilmGenre(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        if (film.getGenres().isEmpty()) {
            film.setGenres(new LinkedHashSet<>());
        }
        final String sqlDelete = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDelete, film.getId());

        final String sqlInsert = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlInsert, film.getId(), genre.getId());
        }
    }

    @Override
    public Set<Genre> loadFilmGenre(Film film) {
        final String sqlQuery =
                "SELECT GENRES.GENRE_ID, GENRE_NAME FROM GENRES" +
                        " JOIN FILMS_GENRES FG ON GENRES.GENRE_ID = FG.GENRE_ID WHERE FILM_ID = ?";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, film.getId()));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getLong("GENRE_ID"),
                resultSet.getString("GENRE_NAME")
        );
    }
}
