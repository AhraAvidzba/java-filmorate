package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long userId, Long filmId) {
        checkUserId(userId);
        checkFilmId(filmId);
        filmStorage.getFilmById(filmId).getUserLikes().add(userId);
    }

    public void removeLike(Long userId, Long filmId) {
        checkUserId(userId);
        checkFilmId(filmId);
        filmStorage.getFilmById(filmId).getUserLikes().remove(userId);
    }

    public List<Film> findPopularFilms(Integer size) {

        if (size == null || size <= 0) {
            throw new RuntimeException("Значение size должно быть больше нуля");
        }
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(x -> x.getUserLikes().size(), Comparator.reverseOrder()))
                .limit(size)
                .collect(Collectors.toList());
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }

    private void checkUserId(Long userId) {
        if (userId == null || userStorage.getUserById(userId) == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
    }

    private void checkFilmId(Long filmId) {
        if (filmId == null || filmStorage.getFilmById(filmId) == null) {
            throw new ContentNotFountException("Фильм не найден");
        }
    }
}
