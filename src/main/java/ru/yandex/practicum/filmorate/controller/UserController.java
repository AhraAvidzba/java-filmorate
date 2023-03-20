package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final static Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        if (users.containsKey(user.getId())) {
            throw new ContentAlreadyExistException("пользователь уже существует");
        }
        user.setName(user.getName() == null ? user.getLogin() : user.getName());
        user.setId(id++);
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Пользователь обновлен");
            return user;
        } else {
            throw new ContentNotFountException("Пользователь не найден");
        }
    }
}
