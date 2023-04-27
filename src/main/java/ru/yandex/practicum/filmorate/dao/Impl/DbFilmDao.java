package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("dbFilmDao")
@RequiredArgsConstructor
public class DbFilmDao implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * " +
                "FROM film AS f ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void removeAllFilms() {
        String sql = "DELETE FROM film";
        jdbcTemplate.execute(sql);
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT * FROM film WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), id);
    }

    @Override
    public Film putFilm(Film film) {
        String sql = "INSERT INTO film(name, description, release_date, duration, rating) " +
                "VALUES (?, ? ,?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setString(5, film.getRating().toString());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        //Сохраняем список лайков к фильму
        fillLikes(film, generatedId);

        //Сохраняем список жанров к фильму
        fillGenres(film, generatedId);

        return getFilmById(generatedId);

    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRating().toString(), film.getId());

        //Сохраняем список лайков к фильму
        String sql2 = "DELETE FROM likes WHERE film_id = ?";
        jdbcTemplate.update(sql2, film.getId());
        fillLikes(film, film.getId());

        //Сохраняем список жанров к фильму
        String sql3 = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql3, film.getId());
        fillGenres(film, film.getId());

        return getFilmById(film.getId());
    }

    @Override
    public void removeFilmById(Long id) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void putLike(Long user_id, Long film_id) {
        String sql = "MERGE INTO likes KEY (film_id, user_id) VALUES(?, ?);";
        jdbcTemplate.update(sql, film_id, user_id);
    }

    @Override
    public void removeLike(Long user_id, Long film_id) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, film_id, user_id);

    }

    private void fillGenres(Film film, Long film_id) {
        String genreSql = "INSERT INTO film_genre(film_id, genre_id) " +
                "VALUES (?, ?)";
        List<Genre> genreList = (new ArrayList<>(film.getGenre()));
        jdbcTemplate.batchUpdate(genreSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film_id);
                ps.setLong(2, getGenreId(genreList.get(i).toString()));
            }

            @Override
            public int getBatchSize() {
                return genreList.size();
            }
        });
    }

    private void fillLikes(Film film, Long film_id) {
        String likesSql = "INSERT INTO likes(film_id, user_id) " +
                "VALUES (?, ?)";

        List<Long> likesList = new ArrayList<>(film.getUserLikes());
        jdbcTemplate.batchUpdate(likesSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, film_id);
                ps.setLong(2, likesList.get(i));
            }

            @Override
            public int getBatchSize() {
                return likesList.size();
            }
        });
    }

    private Long getGenreId(String genre) {
        String sql = "SELECT g.genre_id " +
                "FROM genre AS g " +
                "WHERE g.name = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("genre_id"), genre).get(0);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder().id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rating(Rating.valueOf(rs.getString("rating")))
                .genre(makeGenreList(rs.getLong("film_id")))
                .userLikes(makeLikeList(rs.getLong("film_id")))
                .build();
    }

    private Set<Genre> makeGenreList(Long film_id) throws SQLException {
        String sql = "SELECT g.name " +
                "FROM film_genre AS fg " +
                "JOIN genre AS g ON g.genre_id = fg.genre_id " +
                "JOIN film AS f ON f.film_id = fg.film_id " +
                "WHERE f.film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> Genre.valueOf(rs.getString("name")), film_id));
    }

    private Set<Long> makeLikeList(Long film_id) throws SQLException {
        String sql = "SELECT u.user_id " +
                "FROM likes AS l " +
                "JOIN _user AS u ON u.user_id = l.user_id " +
                "JOIN film AS f ON f.film_id = l.film_id " +
                "WHERE f.film_id = ? ";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), film_id));
    }
}
