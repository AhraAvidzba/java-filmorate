package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Service
public class FilmService {
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long userId, Long filmId) {

    }

    public void removeLike(Long userId, Long filmId) {

    }

    public void findPopularFilms(Integer size) {

    }
}
