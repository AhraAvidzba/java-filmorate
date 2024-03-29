package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Film createdFilm = filmService.putFilm(film);
        log.debug("Фильм добавлен");
        return createdFilm;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        log.debug("Фильм обновлен");
        return updatedFilm;
    }

    @GetMapping("/popular")
    public List<Film> findAll(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable(name = "id") Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") Long filmId, @PathVariable Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(name = "id") Long filmId) {
        return filmService.getFilmById(filmId);
    }
}
