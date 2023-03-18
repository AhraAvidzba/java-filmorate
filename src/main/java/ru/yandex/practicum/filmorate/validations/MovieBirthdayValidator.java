package ru.yandex.practicum.filmorate.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MovieBirthdayValidator implements ConstraintValidator<MovieBirthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value != null && value.isBefore(LocalDate.now())) {
            return value.isAfter(LocalDate.of(1895, 12, 28));
        }
        return false;
    }

}
