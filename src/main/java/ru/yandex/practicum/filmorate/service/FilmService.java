package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Long userId, Long filmId) {
        filmStorage.getFilmById(filmId).getUserLikes().add(userId);
    }

    public void removeLike(Long userId, Long filmId) {
        filmStorage.getFilmById(filmId).getUserLikes().remove(userId);
    }

    public List<Film> findPopularFilms(Integer size) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getRate))
                .limit(size)
                .collect(Collectors.toList());
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
