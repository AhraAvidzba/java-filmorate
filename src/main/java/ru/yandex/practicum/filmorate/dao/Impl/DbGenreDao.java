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
        String sql = "SELECT * " +
                "FROM genre AS g ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getLong("genre_id"),
                Genres.valueOf(rs.getString("name")).getTranslate()));
    }

    @Override
    public Genre getGenreById(Long genreId) {
        String sql = "SELECT DISTINCT * " +
                "FROM genre AS g " +
                "WHERE g.genre_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Genre(rs.getLong("genre_id"),
                Genres.valueOf(rs.getString("name")).getTranslate()), genreId);
    }
}
