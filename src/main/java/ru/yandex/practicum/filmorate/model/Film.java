package ru.yandex.practicum.filmorate.model;
import lombok.*;
import ru.yandex.practicum.filmorate.validations.MovieBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

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
    private Integer rate;

    private final Set<Long> userLikes = Collections.emptySet();

}
