package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.Genres;

@Data
@AllArgsConstructor
public class Genre {
    private Long id;
    private Genres name;
}
