package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> users = new HashMap<>();

    private Long globalId = 1L;

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public void removeAllUsers() {
        users.clear();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User putUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void removeUserById(Long id) {
        users.remove(id);
    }

    @Override
    public void addToFriendsList(Long userId, Long friendId) {
        User friend = users.get(friendId);
        friend.getFriendsList().put(userId, Status.UNCONFIRMED);
    }

    @Override
    public void delFromFriendsList(Long userId, Long friendId) {
        users.get(userId).getFriendsList().remove(friendId);
        users.get(friendId).getFriendsList().remove(userId);
    }

    private Long generateId() {
        return globalId++;
    }
}
