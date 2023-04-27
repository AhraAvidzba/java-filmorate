package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void removeAllUsers();

    User getUserById(Long id);

    User putUser(User user);

    User updateUser(User user);

    void removeUserById(Long id);

    void addToFriendsList(Long userId, Long friendId);

    void delFromFriendsList(Long userId, Long friendId);
}

