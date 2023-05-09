package ru.yandex.practicum.filmorate.dao.Impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmDao implements FilmDao {

    private final Map<Long, Film> films = new HashMap<>();
    private Long globalId = 1L;

    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    public Film putFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void putLike(Long filmId, Long userId) {
        getFilmById(filmId).getUserLikes().add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        getFilmById(filmId).getUserLikes().remove(userId);
    }

    private Long generateId() {
        return globalId++;
    }
}
