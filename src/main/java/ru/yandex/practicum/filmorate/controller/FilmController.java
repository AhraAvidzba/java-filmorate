package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ContentAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final static Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;
    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        //((ch.qos.logback.classic.Logger) log).setLevel(Level.DEBUG);
        if (!films.containsKey(film.getId())) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.debug("Фильм добавлен");
            return film;

        } else {
            throw new ContentAlreadyExistException("Фильм уже существует в медиатеке");
        }
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
