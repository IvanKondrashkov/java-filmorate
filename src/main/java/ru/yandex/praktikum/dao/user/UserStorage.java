package ru.yandex.praktikum.dao.user;

import java.util.List;
import ru.yandex.praktikum.model.User;

public interface UserStorage {
    User findById(Long id);
    List<User> findAll();
    List<User> findAllFriends(Long id);
    List<User> findCommonFriends(Long id, Long otherId);
    User save(User user);
    User update(User user);
    void deleteById(Long id);
    void addFriend(Long id, Long friendId);
    void deleteFriend(Long id, Long friendId);
}
