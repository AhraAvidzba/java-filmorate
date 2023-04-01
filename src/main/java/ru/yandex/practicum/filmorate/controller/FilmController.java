package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getFilmStorage().getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmService.getFilmStorage().putFilm(film);
        log.debug("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        filmService.getFilmStorage().updateFilm(film);
        log.debug("Фильм обновлен");
        return film;
    }

    @GetMapping("/popular")
    public List<Film> findAll(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable(name = "id") Long filmId, @PathVariable Long userId) {
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") Long filmId, @PathVariable Long userId) {
        filmService.removeLike(userId, filmId);
    }

    @GetMapping("/{id}")
    public Film commonFriends(@PathVariable(name = "id") Long filmId) {
        return filmService.getFilmStorage().getFilmById(filmId);
    }
}
