package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllFriends(Long userId) {
        checkUserId(userId);
        return userStorage.getUserById(userId).getFriendsList().stream()
                .map(x -> userStorage.getUserById(x))
                .collect(Collectors.toList());
    }

    public void addToFriendsList(Long userId, Long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        userStorage.getUserById(userId).getFriendsList().add(friendId);
        userStorage.getUserById(friendId).getFriendsList().add(userId);
    }

    public void removeFromFriendsList(Long userId, Long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        userStorage.getUserById(userId).getFriendsList().remove(friendId);
    }

    public List<User> findCommonFriends(Long userId, Long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        Set<Long> userFriendList = userStorage.getUserById(userId).getFriendsList();
        return userStorage.getUserById(friendId).getFriendsList().stream()
                .filter(userFriendList::contains)
                .map(x -> userStorage.getUserById(x))
                .collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    private void checkUserId(Long userId) {
        if (userId == null || userStorage.getUserById(userId) == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
    }
}
