package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.enums.Genres;

import java.util.List;

@Component("dbGenreDao")
@RequiredArgsConstructor
public class DbGenreDao implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT DISTINCT g.genre_id, g.name " +
                "FROM film_genre AS fg " +
                "JOIN genre AS g ON fg.GENRE_ID = g.GENRE_ID";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getLong("genre_id"),
                Genres.valueOf(rs.getString("name"))));
    }

    @Override
    public Genre getGenreById(Long genre_id) {
        String sql = "SELECT DISTINCT g.genre_id, g.name " +
                "FROM film_genre AS fg " +
                "JOIN genre AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE fg.genre_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Genre(rs.getLong("genre_id"),
                Genres.valueOf(rs.getString("name"))), genre_id);
    }
}
