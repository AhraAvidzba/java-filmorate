package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("dbFilmDao")
@RequiredArgsConstructor
public class DbFilmDao implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate nmJdbcTemplate;


    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT f.*, r.name AS rating_name " +
                "FROM film AS f " +
                "LEFT JOIN rating AS r ON r.rating_id = f.rating_id ";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        //Заполнение лайков
        fillLikes(films);
        //Заполнение жанров
        fillGenres(films);
        return films;
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.*, r.name AS rating_name " +
                "FROM film AS f " +
                "LEFT JOIN rating AS r ON r.rating_id = f.rating_id " +
                "WHERE f.film_id = ? ";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
        //Заполнение лайков
        fillLikes(films);
        //Заполнение жанров
        fillGenres(films);
        return films.isEmpty() ? null : films.get(0);
    }

    @Override
    public Film putFilm(Film film) {
        String sql = "INSERT INTO film(name, description, release_date, duration, rating_id) " +
                "VALUES (?, ? ,?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"film_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey().longValue();

        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }

        //Сохраняем список жанров к фильму
        fillGenreTable(film, generatedId);

        return getFilmById(generatedId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        //Сохраняем список жанров к фильму
        String sql3 = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql3, film.getId());
        fillGenreTable(film, film.getId());

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

    private void fillGenreTable(Film film, Long filmId) {
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

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder().id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Rating(rs.getInt("rating_id"), rs.getString("rating_name")))
                .userLikes(Collections.emptySet())
                .genres(Collections.emptySet())
                .build();
    }

    private void fillLikes(List<Film> films) {
        List<Long> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
        Map<String, List<Long>> idsMap = Collections.singletonMap("ids", filmsId);
        String likesSql = "SELECT l.* FROM likes AS l WHERE l.film_id IN (:ids)";
        List<Map<Long, TreeSet<Long>>> likes = nmJdbcTemplate.query(likesSql, idsMap, ((rs, rowNum) -> makeAllLikes(rs)));
        if (!likes.isEmpty()) {
            films.stream()
                    .filter(f -> likes.get(0).containsKey(f.getId()))
                    .forEach(f -> f.setUserLikes(likes.get(0).get(f.getId())));
        }
    }

    private void fillGenres(List<Film> films) {
        List<Long> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
        Map<String, List<Long>> idsMap = Collections.singletonMap("ids", filmsId);
        String genresSql = "SELECT f.film_id, g.genre_id, g.name AS genre_name " +
                "FROM genre AS g " +
                "RIGHT JOIN film_genre AS fg ON g.genre_id = fg.genre_id " +
                "RIGHT JOIN film AS f ON f.film_id = fg.film_id " +
                "WHERE f.film_id IN (:ids)";
        List<Map<Long, TreeSet<Genre>>> genres = nmJdbcTemplate.query(genresSql, idsMap, ((rs, rowNum) -> makeAllGenres(rs)));
        if (!genres.isEmpty()) {
            films.stream()
                    .filter(f -> genres.get(0).containsKey(f.getId()))
                    .forEach(f -> f.setGenres(new HashSet<>(genres.get(0).get(f.getId()))));
        }
    }

    private Map<Long, TreeSet<Genre>> makeAllGenres(ResultSet rs) throws SQLException {
        Map<Long, TreeSet<Genre>> genres = new HashMap<>();
        do {
            Long filmId = rs.getLong("film_id");
            genres.putIfAbsent(filmId, new TreeSet<>(Comparator.comparingLong(Genre::getId)));
            if (rs.getString("genre_name") != null) {
                genres.get(filmId).add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
        } while (rs.next());
        return genres;
    }

    private Map<Long, TreeSet<Long>> makeAllLikes(ResultSet rs) throws SQLException {
        Map<Long, TreeSet<Long>> likes = new HashMap<>();
        do {
            Long filmId = rs.getLong("film_id");
            likes.putIfAbsent(filmId, new TreeSet<>(Long::compare));
            if (rs.getLong("user_id") != 0) {
                likes.get(filmId).add(rs.getLong("user_id"));
            }
        } while (rs.next());
        return likes;
    }

    public void putGenre(Integer id, String name) {
        String sql = "MERGE INTO genre KEY (genre_id) VALUES(?, ?)";
        jdbcTemplate.update(sql, id, name);
    }
}
