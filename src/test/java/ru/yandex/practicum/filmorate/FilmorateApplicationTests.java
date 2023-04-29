package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.Impl.DbFilmDao;
import ru.yandex.practicum.filmorate.dao.Impl.DbUserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.Genres;
import ru.yandex.practicum.filmorate.model.enums.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class FilmorateApplicationTests {
    private final DbUserDao userStorage;
    private final DbFilmDao filmStorage;

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.getAllUsers();
        assertThat(users).asList().hasSize(3);
    }

    @Test
    public void testFindUserById() {
        User user = userStorage.getUserById(1L);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .birthday(LocalDate.of(1990, 2, 10))
                .login("avidzba")
                .name("A")
                .email("avidzva@yandex.ru").build();
        User returnedUser = userStorage.putUser(user);
        assertThat(returnedUser).hasFieldOrPropertyWithValue("login", "avidzba");
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .id(1L)
                .birthday(LocalDate.of(1990, 2, 10))
                .login("akhraa1")
                .name("A")
                .email("avidzva@yandex.ru").build();
        User returnedUser = userStorage.updateUser(user);
        assertThat(returnedUser).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("login", "akhraa1");
    }

    @Test
    public void testAddFriend() {
        User user = userStorage.getUserById(2L);
        System.out.println(user);
        Assertions.assertEquals(user.getFriendsList().get(3L), Status.UNCONFIRMED);
        userStorage.addToFriendsList(2L, 3L);
        User updatedUser = userStorage.getUserById(2L);
        System.out.println(updatedUser);
        Assertions.assertEquals(updatedUser.getFriendsList().get(3L), Status.CONFIRMED);
    }

    @Test
    public void testDelFriend() {
        User user = userStorage.getUserById(2L);
        Assertions.assertEquals(user.getFriendsList().get(3L), Status.UNCONFIRMED);
        userStorage.delFromFriendsList(3L, 2L);
        User updatedUser = userStorage.getUserById(2L);
        Assertions.assertNull(updatedUser.getFriendsList().get(3L));
    }


    @Test
    public void testFindAlFilms() {
        List<Film> films = filmStorage.getAllFilms();
        assertThat(films).asList().hasSize(3);
    }

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.getFilmById(1L);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testPutFilm() {
        Film film = Film.builder()
                .name("Тест")
                .releaseDate(LocalDate.of(1990, 4, 7))
                .duration(200)
                .mpa(new Rating(1, null))
                .build();

        Film returnedFilm = filmStorage.putFilm(film);
        assertThat(returnedFilm).hasFieldOrPropertyWithValue("name", "Тест");
    }

    @Test
    public void testUpdateFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("Тест")
                .releaseDate(LocalDate.of(1990, 4, 7))
                .duration(200)
                .mpa(new Rating(1, null))
                .build();
        Film returnedFilm = filmStorage.updateFilm(film);
        assertThat(returnedFilm).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "Тест");
    }

    @Test
    public void testPutLike() {
        Film film = filmStorage.getFilmById(2L);
        Assertions.assertFalse(film.getUserLikes().contains(1L));
        filmStorage.putLike(2L, 1L);
        Film updatedFilm = filmStorage.getFilmById(2L);
        Assertions.assertTrue(updatedFilm.getUserLikes().contains(1L));
    }

    @Test
    public void testRemoveLike() {
        Film film = filmStorage.getFilmById(2L);
        Assertions.assertTrue(film.getUserLikes().contains(3L));
        filmStorage.removeLike(2L, 3L);
        Film updatedFilm = filmStorage.getFilmById(2L);
        Assertions.assertFalse(updatedFilm.getUserLikes().contains(3L));
    }

    @BeforeEach
    public void fillDB() {
        //Заполнение жанров
        filmStorage.putGenre(1, Genres.COMEDY.toString());
        filmStorage.putGenre(2, Genres.DRAMA.toString());
        filmStorage.putGenre(3, Genres.CARTOON.toString());
        filmStorage.putGenre(4, Genres.THRILLER.toString());
        filmStorage.putGenre(5, Genres.DOCUMENTARY.toString());
        filmStorage.putGenre(6, Genres.ACTION.toString());

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
                .genres(Set.of(new Genre(1L, Genres.ACTION.toString())))
                .build();
        filmStorage.putFilm(film1);

        Film film2 = Film.builder()
                .name("Мстители: Финал")
                .releaseDate(LocalDate.of(2019, 4, 22))
                .duration(181)
                .mpa(new Rating(2, null))
                .description("test")
                .genres(Set.of(new Genre(1L, Genres.ACTION.toString())))
                .build();
        filmStorage.putFilm(film2);

        Film film3 = Film.builder()
                .name("Титаник")
                .releaseDate(LocalDate.of(1997, 11, 1))
                .duration(181)
                .mpa(new Rating(3, null))
                .description("test")
                .genres(Set.of(new Genre(1L, Genres.ACTION.toString())))
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