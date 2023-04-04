package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getAllFilms();

    void removeAllFilms();

    Film getFilmById(Long id);

    Film putFilm(Film task);

    Film updateFilm(Film task);

    void removeFilmById(Long id);
}
