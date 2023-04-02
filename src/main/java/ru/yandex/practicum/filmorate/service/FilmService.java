package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new ContentNotFountException("Фильм не найден");
        }
        return film;
    }

    public Film putFilm(Film film) {
        if (filmStorage.getAllFilms().contains(film)) {
            throw new ContentAlreadyExistException("Фильм уже присутствует в медиатеке");
        }
        return filmStorage.putFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null || !filmStorage.getAllFilms().contains(film)) {
            throw new ContentNotFountException("Фильм не найден");
        }
        return filmStorage.updateFilm(film);
    }

    public void addLike(Long userId, Long filmId) {
        checkUserId(userId);
        checkAndReturnFilm(filmId).getUserLikes().add(userId);
    }

    public void removeLike(Long userId, Long filmId) {
        checkUserId(userId);
        checkAndReturnFilm(filmId).getUserLikes().remove(userId);
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

    private void checkUserId(Long userId) {
        User user = userStorage.getUserById(userId);
        if (userId == null || user == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }

    }

    private Film checkAndReturnFilm(Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (filmId == null || film == null) {
            throw new ContentNotFountException("Фильм не найден");
        }
        return film;
    }
}
