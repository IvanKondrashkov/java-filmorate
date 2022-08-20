package ru.yandex.praktikum.dao.user;

import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.User;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findById(Long id) {
        final String sqlQuery = "SELECT USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY FROM USERS WHERE USER_ID = ?";
        if (id < 1) {
            throw new NotFoundException(String.format("User with id=%d not found!", id));
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> findAll() {
        final String sqlQuery = "SELECT USER_ID, USER_NAME, LOGIN, EMAIL, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public List<User> findAllFriends(Long id) {
        final String sqlQuery = "SELECT U.* FROM USERS U JOIN FRIENDS F on U.USER_ID = F.FRIEND_ID WHERE F.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        final String sqlQuery =
                "SELECT U.* FROM USERS U" +
                        " JOIN FRIENDS F on U.USER_ID = F.FRIEND_ID" +
                        " JOIN FRIENDS F2 on U.USER_ID = F2.FRIEND_ID WHERE F.USER_ID = ? AND F2.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
    }

    @Override
    public User save(User user) {
        final String sqlQuery = "INSERT INTO USERS(USER_NAME, LOGIN, EMAIL, BIRTHDAY) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
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
        final String sqlQuery = "UPDATE USERS SET USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getLogin()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
        return findById(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        final String sqlQuery = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        final String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
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
