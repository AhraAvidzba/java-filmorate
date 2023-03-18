package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Data
@Builder
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
        assertEquals("name: не должно быть пустым", ex.getMessage());
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
        assertEquals("description: размер должен находиться в диапазоне от 0 до 200", ex.getMessage());
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
        assertEquals("releaseDate: Фильм должен быть года выпуска не ранее 28.12.1895г и не позднее настоящего времени", ex.getMessage());
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
        assertEquals("releaseDate: Фильм должен быть года выпуска не ранее 28.12.1895г и не позднее настоящего времени", ex.getMessage());
    }

}
