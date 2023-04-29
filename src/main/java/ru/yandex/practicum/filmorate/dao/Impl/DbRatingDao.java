package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.enums.Ratings;

import java.util.List;

@Component("dbRatingDao")
@RequiredArgsConstructor
public class DbRatingDao implements RatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT DISTINCT f.rating " +
                "FROM film AS f ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Rating(Ratings.getIdByRating(Ratings.valueOf(rs.getString("rating"))),
                Ratings.valueOf(rs.getString("rating"))));
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        String ratingName = Ratings.getRatingById(ratingId).toString();
        String sql = "SELECT DISTINCT f.rating " +
                "FROM film AS f " +
                "WHERE f.RATING = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Rating(Ratings.getIdByRating(Ratings.valueOf(rs.getString("rating"))),
                Ratings.valueOf(rs.getString("rating"))), ratingName);
    }
}
