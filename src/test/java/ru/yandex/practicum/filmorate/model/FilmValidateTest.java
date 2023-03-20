package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidateTest {

    public static void validateInput(Film film) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Test
    public void shouldThrowExceptionWhenNameEmpty() {
        Film film = Film.builder()
                .name("")
                .description("Пандора")
                .releaseDate(LocalDate.of(2009, 12, 18))
                .duration(162L)
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(film)
        );
    }

    @Test
    public void shouldThrowExceptionWhenTooLongDescription() {
        Film film = Film.builder()
                .name("Аватар")
                .description("Пандора".repeat(30))
                .releaseDate(LocalDate.of(2009, 12, 18))
                .duration(162L)
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(film)
        );
    }

    @Test
    public void shouldThrowExceptionWhenReleaseTooEarly() {
        Film film = Film.builder()
                .name("Аватар")
                .description("Пандора")
                .releaseDate(LocalDate.of(1700, 12, 18))
                .duration(162L)
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(film)
        );
    }

    @Test
    public void shouldThrowExceptionWhenReleaseInFuture() {
        Film film = Film.builder()
                .name("Аватар")
                .description("Пандора")
                .releaseDate(LocalDate.of(3000, 12, 18))
                .duration(162L)
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(film)
        );
    }

}
