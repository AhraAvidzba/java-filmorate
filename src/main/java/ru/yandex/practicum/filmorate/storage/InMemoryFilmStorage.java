package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements  FilmStorage {

    private final Map<Long, Film> films= new HashMap<>();
    private Long globalId = 1L;
    @Override
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public void removeAllFilms() {
        films.clear();
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new ContentNotFountException("Фильм не найден");
        }
        return films.get(id);
    }

    @Override
    public Long putFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw  new ContentAlreadyExistException("Фильм уже присутствует в медиатеке");
        }
        film.setId(generateId());
        films.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public void updateFilm(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new ContentNotFountException("Фильм не найден");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void removeFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new ContentNotFountException("Фильм не найден");
        }
        films.remove(id);
    }

    private Long generateId() {
        return globalId++;
    }
}
