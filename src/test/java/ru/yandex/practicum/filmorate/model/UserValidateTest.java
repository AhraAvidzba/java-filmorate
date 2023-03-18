package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UserValidateTest {

    public static void validateInput(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Test
    public void shouldThrowExceptionWhenDateInFuture() {
        User user1 = User.builder()
                .birthday(LocalDate.of(3000,8,30))
                .login("akhraa1")
                .email("akhraa1@yandex.ru")
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(user1)
        );
        assertEquals("birthday: должно содержать прошедшую дату", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmpty() {
        User user1 = User.builder()
                .birthday(LocalDate.of(1992,8,30))
                .login("")
                .email("akhraa1@yandex.ru")
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(user1)
        );
        assertEquals("login: не должно быть пустым", ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailNotValid() {
        User user1 = User.builder()
                .birthday(LocalDate.of(1992,8,30))
                .login("akhraa1")
                .email("333")
                .build();
        ConstraintViolationException ex = assertThrows(
                ConstraintViolationException.class,
                () -> validateInput(user1)
        );
        assertEquals("email: должно иметь формат адреса электронной почты", ex.getMessage());
    }

}


