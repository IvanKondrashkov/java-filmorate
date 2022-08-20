package ru.yandex.praktikum.dao.film;

import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import ru.yandex.praktikum.model.User;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.praktikum.dao.user.UserStorage;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findById(Long id) {
        final String sqlQuery = "select USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY from USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> findAll() {
        final String sqlQuery = "select USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public List<User> findAllFriends(Long id) {
        return null;
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        return null;
    }

    @Override
    public User save(User user) {
        final String sqlQuery = "insert into USERS(USER_NAME, LOGIN, EMAIL, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        final String sqlQuery = "update USERS set USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getLogin()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {

    }

    @Override
    public void deleteFriend(Long id, Long friendId) {

    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getLong("USER_ID"),
                resultSet.getString("USER_NAME"),
                resultSet.getString("LOGIN"),
                resultSet.getString("EMAIL"),
                resultSet.getDate("BIRTHDAY").toLocalDate()
        );
    }
}
