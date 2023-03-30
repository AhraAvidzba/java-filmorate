package ru.yandex.practicum.filmorate.model;
import lombok.*;
import ru.yandex.practicum.filmorate.validations.MovieBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Long id;
    @EqualsAndHashCode.Exclude
    @NotBlank
    private String name;
    @EqualsAndHashCode.Exclude
    @Size(max = 200)
    private String description;
    @EqualsAndHashCode.Exclude
    @MovieBirthday
    private LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    @Positive
    private Long duration;
}
