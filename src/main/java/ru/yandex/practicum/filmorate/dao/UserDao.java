package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUserById(Long id);

    User putUser(User user);

    User updateUser(User user);

    void addToFriendsList(Long userId, Long friendId);

    void delFromFriendsList(Long userId, Long friendId);
}

