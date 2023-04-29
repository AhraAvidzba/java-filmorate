package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.ContentNotFountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final RatingDao ratingDao;

    @Autowired
    public FilmService(@Qualifier("dbFilmDao") FilmDao filmDao, @Qualifier("dbUserDao") UserDao userDao, GenreDao genreDao, RatingDao ratingDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
        this.genreDao = genreDao;
        this.ratingDao = ratingDao;
    }

    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    public Film getFilmById(Long id) {
        Film film = filmDao.getFilmById(id);
        if (film == null) {
            throw new ContentNotFountException("Фильм не найден");
        }
        return film;
    }

    public Film putFilm(Film film) {
        return filmDao.putFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null || getFilmById(film.getId()) == null) {
            throw new ContentNotFountException("Фильм не найден");
        }
        return filmDao.updateFilm(film);
    }

    public void addLike(Long userId, Long filmId) {
        checkUserId(userId);
        checkFilmId(filmId);
        filmDao.putLike(userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        checkUserId(userId);
        checkFilmId(filmId);
        filmDao.removeLike(userId, filmId);
    }

    public List<Film> findPopularFilms(Integer size) {

        if (size == null || size <= 0) {
            throw new RuntimeException("Значение size должно быть больше нуля");
        }
        return filmDao.getAllFilms().stream()
                .sorted(Comparator.comparing(x -> x.getUserLikes().size(), Comparator.reverseOrder()))
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Genre> getGenres() {
        return genreDao.getGenres();
    }

    public Genre getGenreById(Long genreId) {
        if (genreDao.getGenres().stream().map(Genre::getId).anyMatch(x -> Objects.equals(x, genreId))) {
            return genreDao.getGenreById(genreId);
        }
        throw new ContentNotFountException(String.format("Жанр с id %d не найден", genreId));
    }

    private void checkUserId(Long userId) {
        User user = userDao.getUserById(userId);
        if (userId == null || user == null) {
            throw new ContentNotFountException("Пользователь не найден");
        }
    }

    public List<Rating> getRatings() {
        return ratingDao.getRatings();
    }

    public Rating getRatingById(Integer ratingId) {
        if (ratingDao.getRatings().stream().map(Rating::getId).anyMatch(x -> Objects.equals(x, ratingId))) {
            return ratingDao.getRatingById(ratingId);
        }
        throw new ContentNotFountException(String.format("Рейтинг с id %d не найден", ratingId));
    }

    private void checkFilmId(Long filmId) {
        Film film = filmDao.getFilmById(filmId);
        if (filmId == null || film == null) {
            throw new ContentNotFountException("Фильм не найден");
        }
    }
}
