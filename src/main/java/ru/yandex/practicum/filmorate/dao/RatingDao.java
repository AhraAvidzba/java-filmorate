package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingDao {
    List<Rating> getRatings();

    Rating getRatingById(Integer rating_id);
}
