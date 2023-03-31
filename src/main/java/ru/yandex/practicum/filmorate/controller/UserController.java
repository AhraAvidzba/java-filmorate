package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public Collection<User> findAll() {
        return userService.getUserStorage().getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        userService.getUserStorage().putUser(user);
        log.debug("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        userService.getUserStorage().updateUser(user);
        log.debug("Пользователь обновлен");
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(name = "id") Long userId, @PathVariable Long friendId) {
        userService.addToFriendsList(userId, friendId);
        log.debug("Пользователь добавлен в друзья");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable(name = "id") Long userId, @PathVariable Long friendId) {
        userService.removeFromFriendsList(userId, friendId);
        log.debug("Пользователь удален из друзья");
    }

    @GetMapping("/{id}/friends")
    public void findFriends(@PathVariable(name = "id") Long userId) {
        userService.findAllFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public void commonFriends(@PathVariable(name = "id") Long userId, @PathVariable Long otherId) {
        userService.findCommonFriends(userId, otherId);
    }
}
