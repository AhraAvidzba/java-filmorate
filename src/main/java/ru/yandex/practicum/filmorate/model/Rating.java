package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.Ratings;

@Data
@AllArgsConstructor
public class Rating {
    Integer id;
    Ratings name;
}
