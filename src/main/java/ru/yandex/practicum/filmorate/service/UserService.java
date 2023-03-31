package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Set<User> findAllFriends(Long userId) {
        return userStorage.getUserById(userId).getFriendsList();
    }

    public void addToFriendsList(Long userId, Long friendId) {
        User friend = userStorage.getUserById(friendId);
        userStorage.getUserById(userId).getFriendsList().add(friend);
    }

    public void removeFromFriendsList(Long userId, Long friendId) {
        User friend = userStorage.getUserById(friendId);
        userStorage.getUserById(userId).getFriendsList().remove(friend);
    }

    public List<User> findCommonFriends(Long userId, Long friendId) {
        Set<User> userFriendList = userStorage.getUserById(userId).getFriendsList();
        return userStorage.getUserById(friendId).getFriendsList().stream()
                .filter(userFriendList::contains)
                .collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

}
