package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.Status;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(@Qualifier("dbUserDao") UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        return user;
    }

    public User putUser(User user) {
        List<User> matchedUsers = userDao.getAllUsers().stream()
                .filter(x -> x.getEmail().equals(user.getEmail()) || x.getLogin().equals(user.getLogin()))
                .collect(Collectors.toList());
        if (!matchedUsers.isEmpty()) {
            throw new ContentAlreadyExistException("Пользователь с таким логином или емэйлом уже присутствует в базе данных");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userDao.putUser(user);
    }

    public User updateUser(User user) {
        if (user.getId() == null || getUserById(user.getId()) == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userDao.updateUser(user);
    }

    public List<User> findAllFriends(Long userId) {
        User user = checkAndReturnUser(userId);
        return user.getFriendsList().keySet().stream()
                .map(userDao::getUserById)
                .collect(Collectors.toList());
    }

    public void addToFriendsList(Long userId, Long friendId) {
        User user = checkAndReturnUser(userId);
        User friend = checkAndReturnUser(friendId);
        if (user.getFriendsList().containsKey(friendId)) {
            if (user.getFriendsList().get(friendId).equals(Status.CONFIRMED)) {
                throw new ContentAlreadyExistException("Пользователи уже дружат");
            }
        } else if (friend.getFriendsList().containsKey(userId)) {
            throw new ContentAlreadyExistException("Запрос уже был отправлен ранее");
        }
        userDao.addToFriendsList(userId, friendId);
    }

    public void delFromFriendsList(Long userId, Long friendId) {
        User user = checkAndReturnUser(userId);
        User friend = checkAndReturnUser(friendId);
        if (!user.getFriendsList().containsKey(friendId)
                && !friend.getFriendsList().containsKey(userId)) {
            throw new ContentNotFountException("Запросы на дружбу от указанных пользователей друг другу не отправлялись");
        }
        if (user.getFriendsList().containsKey(friendId)
                && user.getFriendsList().get(friendId).equals(Status.UNCONFIRMED)) {
            System.out.println(user);
            throw new ContentNotFountException(String.format("Запрос пользователю с id %s на добавление в друзья " +
                    "от пользователя с id %s еще не был одобрен", friendId, userId));
        }
        userDao.delFromFriendsList(userId, friendId);

    }

    public List<User> findCommonFriends(Long userId, Long friendId) {
        checkAndReturnUser(userId);
        checkAndReturnUser(friendId);
        Set<Long> userFriendList = userDao.getUserById(userId).getFriendsList().keySet();
        return userDao.getUserById(friendId).getFriendsList().keySet().stream()
                .filter(userFriendList::contains)
                .map(userDao::getUserById)
                .collect(Collectors.toList());
    }

    private User checkAndReturnUser(Long userId) {
        User user = userDao.getUserById(userId);
        if (userId == null || user == null || user.getId() == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        return user;
    }
}
