package ru.yandex.practicum.filmorate.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MovieBirthdayValidator.class)
@Documented
public @interface MovieBirthday {

    String message() default "Фильм должен быть года выпуска не ранее 28.12.1895г и не позднее настоящего времени";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}