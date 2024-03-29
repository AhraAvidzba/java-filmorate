package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validations.MovieBirthday;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class Film {
    private Set<Long> userLikes;
    private Set<Genre> genres;
    private Long id;
    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Не должно быть пустым")
    private String name;
    @EqualsAndHashCode.Exclude
    @Size(max = 200, message = "Количество символов должно быть менее 200")
    private String description;
    @EqualsAndHashCode.Exclude
    @MovieBirthday
    private LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    @Positive(message = "Должно быть больше нуля")
    private Integer duration;
    private Rating mpa;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("birthday", name);
        values.put("login", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rating", mpa);
        return values;
    }
}
