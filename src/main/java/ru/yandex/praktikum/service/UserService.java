package ru.yandex.praktikum.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.praktikum.dao.user.UserStorage;
import ru.yandex.praktikum.exception.NotFoundException;
import ru.yandex.praktikum.model.User;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User findById(Long id) {
        final User user = userStorage.findById(id);

        if (Objects.isNull(user)) {
            throw new NotFoundException(String.format("User with id=%d not found!", id));
        }
        return user;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public List<User> findAllFriends(Long id) {
        final User user = userStorage.findById(id);

        if (Objects.isNull(user)) {
            throw new NotFoundException(String.format("User with id=%d not found!", id));
        }
        return userStorage.findAllFriends(id);
    }

    public List<User> findCommonFriends(Long id, Long otherId) {
        final User user = userStorage.findById(id);
        final User other = userStorage.findById(otherId);

        if (Objects.isNull(user) || Objects.isNull(other)) {
            throw new NotFoundException(String.format("Users with id=%d or otherId=%d not found!", id, otherId));
        }
        return userStorage.findCommonFriends(id, otherId);
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
        user = userStorage.update(user);

        if (Objects.isNull(user)) {
            throw new NotFoundException("User with not found!");
        }
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        final User user = userStorage.findById(id);
        final User friend = userStorage.findById(friendId);

        if (Objects.isNull(user) || Objects.isNull(friend)) {
            throw new NotFoundException(String.format("Users with id=%d or friendId=%d not found!", id, friendId));
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        final User user = userStorage.findById(id);
        final User friend = userStorage.findById(friendId);

        if (Objects.isNull(user) || Objects.isNull(friend)) {
            throw new NotFoundException(String.format("Users with id=%d or friendId=%d not found!", id, friendId));
        }
        userStorage.deleteFriend(id, friendId);
    }
}
