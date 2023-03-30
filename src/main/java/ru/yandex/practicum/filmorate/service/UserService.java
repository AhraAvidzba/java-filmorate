package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserStorage userStorage;
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllFriends(Long userId) {
        return Collections.emptyList();
    }

    public void addToFriendsList(Long userId, Long friendId) {

    }

    public void removeFromFriendsList(Long userId, Long friendId) {

    }

    public void findCommonFriends(Long userId, Long friendId) {

    }

}
