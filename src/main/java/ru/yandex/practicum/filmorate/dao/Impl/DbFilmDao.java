package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.enums.Genres;
import ru.yandex.practicum.filmorate.model.enums.Ratings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("dbFilmDao")
@RequiredArgsConstructor
public class DbFilmDao implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, g.name AS genre_name, g.genre_id, l.user_id AS liked_user " +
                "FROM film_genre AS fg " +
                "RIGHT JOIN film AS f ON fg.film_id = f.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN likes AS l ON fg.film_id = l.film_id ";
        return jdbcTemplate.query(sql, this::makeFilms);
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.*, g.name AS genre_name, g.genre_id AS genre_id, l.user_id AS liked_user " +
                "FROM film_genre AS fg " +
                "RIGHT JOIN film AS f ON fg.film_id = f.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN likes AS l ON fg.film_id = l.film_id " +
                "WHERE f.film_id = ? ";

        List<Film> films = jdbcTemplate.query(sql, this::makeFilms, id);
        assert films != null;
        return films.isEmpty() ? null : films.get(0);
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
            ps.setString(5, Ratings.getRatingById(film.getMpa().getId()).toString());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        if (film.getUserLikes() == null) {
            film.setUserLikes(new HashSet<>());
        }

        //Сохраняем список лайков к фильму
        fillLikes(film, generatedId);

        //Сохраняем список жанров к фильму
        fillGenres(film, generatedId);

        return getFilmById(generatedId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), Ratings.getRatingById(film.getMpa().getId()).toString(), film.getId());

        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        if (film.getUserLikes() == null) {
            film.setUserLikes(new HashSet<>());
        }

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
    public void putLike(Long filmId, Long userId) {
        String sql = "MERGE INTO likes KEY (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    private void fillGenres(Film film, Long filmId) {
        String genreSql = "INSERT INTO film_genre(film_id, genre_id) " +
                "VALUES (?, ?)";
        List<Genre> genresList = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(genreSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genresList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genresList.size();
            }
        });
    }

    private void fillLikes(Film film, Long filmId) {
        String likesSql = "INSERT INTO likes(film_id, user_id) " +
                "VALUES (?, ?)";

        List<Long> likesList = new ArrayList<>(film.getUserLikes());
        jdbcTemplate.batchUpdate(likesSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, likesList.get(i));
            }

            @Override
            public int getBatchSize() {
                return likesList.size();
            }
        });
    }

    private List<Film> makeFilms(ResultSet rs) throws SQLException {
        Map<Long, Set<Long>> likes = new HashMap<>();
        Map<Long, Set<Genre>> genres = new HashMap<>();
        Map<Long, String> names = new HashMap<>();
        Map<Long, String> descriptions = new HashMap<>();
        Map<Long, LocalDate> releaseDates = new HashMap<>();
        Map<Long, Integer> durations = new HashMap<>();
        Map<Long, Rating> ratings = new HashMap<>();

        while (rs.next()) {
            Long filmId = rs.getLong("film_id");

            likes.putIfAbsent(filmId, new HashSet<>());
            if (rs.getLong("liked_user") != 0) {
                Long like = rs.getLong("liked_user");
                likes.get(filmId).add(like);
            }

            genres.putIfAbsent(filmId, new HashSet<>());
            if (rs.getString("genre_name") != null) {
                Genre genre = new Genre(rs.getLong("genre_id"), Genres.valueOf(rs.getString("genre_name")).getTranslate());
                genres.get(filmId).add(genre);
            }

            names.putIfAbsent(filmId, rs.getString("name"));
            descriptions.putIfAbsent(filmId, rs.getString("description"));
            releaseDates.putIfAbsent(filmId, rs.getDate("release_date").toLocalDate());
            durations.putIfAbsent(filmId, rs.getInt("duration"));
            ratings.putIfAbsent(filmId, new Rating(Ratings.getIdByRating(Ratings.valueOf(rs.getString("rating"))),
                    Ratings.valueOf(rs.getString("rating")).getName()));
        }

        return names.keySet().stream().map(id -> {
            return Film.builder()
                    .id(id)
                    .name(names.get(id))
                    .description(descriptions.get(id))
                    .releaseDate(releaseDates.get(id))
                    .duration(durations.get(id))
                    .mpa(ratings.get(id))
                    .genres(genres.get(id).stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new)))
                    .userLikes(likes.get(id))
                    .build();
        }).collect(Collectors.toList());
    }

    public void putGenre(Integer id, String name) {
        String sql = "MERGE INTO genre KEY (genre_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, id, name);
    }
}
