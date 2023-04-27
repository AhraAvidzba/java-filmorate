package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        User createdUser = userService.putUser(user);
        log.debug("Пользователь добавлен");
        return createdUser;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        log.debug("Пользователь обновлен");
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(name = "id") Long userId, @PathVariable Long friendId) {
        userService.addToFriendsList(userId, friendId);
        log.debug("Пользователь c id {} добавлен в друзья пользователя с id {}", userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable(name = "id") Long userId, @PathVariable Long friendId) {
        userService.delFromFriendsList(userId, friendId);
        log.debug("Пользователь удален из друзей");
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable(name = "id") Long userId) {
        return userService.findAllFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable(name = "id") Long userId, @PathVariable Long otherId) {
        return userService.findCommonFriends(userId, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(name = "id") Long userId) {
        return userService.getUserById(userId);
    }
}
