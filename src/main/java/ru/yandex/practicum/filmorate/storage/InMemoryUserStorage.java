package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements  UserStorage {

    private final Map<Long, User> users= new HashMap<>();

    private Long globalId;
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
        if (!users.containsKey(id)) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        return users.get(id);
    }

    @Override
    public Long putUser(User user) {
        if (users.containsKey(user.getId())) {
            throw  new ContentAlreadyExistException("Пользователь уже присутствует в базе данных");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        if (user.getId() == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void removeUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new ContentNotFountException("Пользователь не найден");
        }
        users.remove(id);
    }

    private Long generateId() {
        return globalId++;
    }
}
