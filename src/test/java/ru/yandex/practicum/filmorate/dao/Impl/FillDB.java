package ru.yandex.practicum.filmorate.dao.Impl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

public class FillDB {
    public static void fillDB(DbUserDao userStorage, DbFilmDao filmStorage) {
        //Заполнение таблицы с пользователями
        User user1 = User.builder()
                .birthday(LocalDate.of(1990, 2, 10))
                .login("akhra")
                .name("Ахра")
                .email("akhraa1@yandex.ru")
                .birthday(LocalDate.of(1992, 8, 30))
                .build();
        userStorage.putUser(user1);

        User user2 = User.builder()
                .birthday(LocalDate.of(1990, 2, 10))
                .login("julia")
                .name("Юлия")
                .email("julia@gmail.com")
                .birthday(LocalDate.of(1990, 1, 17))
                .build();
        userStorage.putUser(user2);

        User user3 = User.builder()
                .birthday(LocalDate.of(1990, 2, 10))
                .login("anri")
                .name("Анри")
                .email("anri@yandex.ru")
                .birthday(LocalDate.of(1994, 4, 17))
                .build();
        userStorage.putUser(user3);

        //Заполнение таблицы с фильмами
        Film film1 = Film.builder()
                .name("Аватар")
                .releaseDate(LocalDate.of(2009, 12, 10))
                .duration(162)
                .mpa(new Rating(1, null))
                .description("test")
                .genres(Set.of(new Genre(1L, null)))
                .build();
        filmStorage.putFilm(film1);

        Film film2 = Film.builder()
                .name("Мстители: Финал")
                .releaseDate(LocalDate.of(2019, 4, 22))
                .duration(181)
                .mpa(new Rating(2, null))
                .description("test")
                .genres(Set.of(new Genre(1L, null)))
                .build();
        filmStorage.putFilm(film2);

        Film film3 = Film.builder()
                .name("Титаник")
                .releaseDate(LocalDate.of(1997, 11, 1))
                .duration(181)
                .mpa(new Rating(3, null))
                .description("test")
                .genres(Set.of(new Genre(1L, null)))
                .build();
        filmStorage.putFilm(film3);

        //Заполнение лайками
        filmStorage.putLike(1L, 1L);
        filmStorage.putLike(1L, 2L);
        filmStorage.putLike(1L, 3L);
        filmStorage.putLike(3L, 1L);
        filmStorage.putLike(3L, 2L);
        filmStorage.putLike(2L, 3L);

        //Заполнение информации о дружбе
        userStorage.addToFriendsList(1L, 2L);
        userStorage.addToFriendsList(2L, 1L);
        userStorage.addToFriendsList(3L, 2L);
        userStorage.addToFriendsList(1L, 3L);
        userStorage.addToFriendsList(3L, 1L);
    }
}
