package ru.yandex.praktikum.dao.user;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.yandex.praktikum.model.User;

@Component
public class InMemoryUserStorage implements UserStorage {
    private AtomicLong idCurrent = new AtomicLong();
    private final Map<Long, User> users = new LinkedHashMap<>();

    private Long getIdCurrent() {
        return idCurrent.incrementAndGet();
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<User> findAllFriends(Long id) {
        return users.get(id).getFriends().stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Long id, Long otherId) {
        return users.get(id).getFriends().stream()
                .filter(key -> users.get(otherId).getFriends().contains(key))
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public User save(User user) {
        user.setId(getIdCurrent());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            return null;
        }
        users.put(user.getId(), user);
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        if (!users.get(id).getFriends().contains(friendId)) {
            users.get(id).getFriends().add(friendId);
            users.get(friendId).getFriends().add(id);
        }
    }

    public void deleteFriend(Long id, Long friendId) {
        if (users.get(id).getFriends().contains(friendId)) {
            users.get(id).getFriends().remove(friendId);
            users.get(friendId).getFriends().remove(id);
        }
    }
}
