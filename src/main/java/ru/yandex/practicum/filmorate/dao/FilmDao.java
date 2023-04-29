package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {

    List<Film> getAllFilms();

    Film getFilmById(Long id);

    Film putFilm(Film task);

    Film updateFilm(Film task);

    void putLike(Long film_id, Long user_id);

    void removeLike(Long film_id, Long user_id);

}
