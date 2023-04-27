package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    List<Film> getAllFilms();

    void removeAllFilms();

    Film getFilmById(Long id);

    Film putFilm(Film task);

    Film updateFilm(Film task);

    void removeFilmById(Long id);

    void putLike(Long film_id, Long user_id);

    void removeLike(Long film_id, Long user_id);

}
