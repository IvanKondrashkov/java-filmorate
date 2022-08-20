package ru.yandex.praktikum.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        log.info("Send get request /users/{}", id);
        return userService.findById(id);
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Send get request /users");
        return userService.findAll();
    }

    @GetMapping("/{id}/friends")
    public List<User> findAllFriends(@PathVariable Long id) {
        log.info("Send get request /users/{}/friends", id);
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Send get request /users/{}/friends/common/{}", id, otherId);
        return userService.findCommonFriends(id, otherId);
    }

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        log.info("Send post request /users {}", user);
        return userService.save(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Send put request /users {}", user);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Send delete request /users/{}", id);
        userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Send put request /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Send delete request /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }
}
