package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.RatingDao;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("dbRatingDao")
@RequiredArgsConstructor
public class DbRatingDao implements RatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Rating> getRatings() {
        String sql = "SELECT r.* " +
                "FROM rating AS r ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Rating getRatingById(Integer ratingId) {
        String sql = "SELECT r.* " +
                "FROM rating AS r " +
                "WHERE r.rating_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeRating(rs), ratingId);
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        return new Rating(rs.getInt("rating_id"),
                rs.getString("name"));
    }
}
