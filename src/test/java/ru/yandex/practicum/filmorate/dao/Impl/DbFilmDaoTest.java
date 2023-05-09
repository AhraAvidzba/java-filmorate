package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class DbFilmDaoTest {
    private final DbUserDao userStorage;
    private final DbFilmDao filmStorage;

    @BeforeEach
    public void fillDb() {
        FillDB.fillDB(userStorage, filmStorage);
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
}
