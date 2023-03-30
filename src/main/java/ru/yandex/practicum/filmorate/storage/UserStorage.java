package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    void removeAllUsers();

    User getUserById(Long id);

    Long putUser(User user);

    void updateUser(User user);

    void removeUserById(Long id);
}

