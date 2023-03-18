package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new ContentAlreadyExistException("Фильм уже существует в медиатеке");
        }
        film.setId(id++);
        films.put(film.getId(), film);
        log.debug("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм обновлен");
            return film;
        } else {
            throw new ContentNotFountException("Фильм не найден");
        }
    }
}
