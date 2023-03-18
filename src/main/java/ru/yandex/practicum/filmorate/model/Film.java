package ru.yandex.practicum.filmorate.model;
import lombok.*;
import ru.yandex.practicum.filmorate.validations.MovieBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;
    @EqualsAndHashCode.Exclude @NotBlank private final String name;
    @EqualsAndHashCode.Exclude @Size(max = 200) private final String description;
    @EqualsAndHashCode.Exclude @MovieBirthday private final LocalDate releaseDate;
    @EqualsAndHashCode.Exclude @Positive private Long duration;
}
