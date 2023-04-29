package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("dbUserDao")
@RequiredArgsConstructor
public class DbUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate nmJdbcTemplate;

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * " +
                "FROM _user AS u ";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }


    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM _user WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public User putUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("_user")
                .usingGeneratedKeyColumns("user_id");
        Long generatedId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        return getUserById(generatedId);
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE _user SET birthday = ?, login = ?, email = ?, name = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getBirthday(), user.getLogin(), user.getEmail(), user.getName(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public void addToFriendsList(Long userId, Long friendId) {
        String sql;
        String status;
        String rowSetSql = "SELECT * " +
                "FROM friendship AS f " +
                "WHERE (user_id = :userId AND friend_id = :friendId) " +
                "OR (user_id = :friendId AND friend_id = :userId)";
        SqlRowSet rowSet = nmJdbcTemplate.queryForRowSet(rowSetSql, Map.of("userId", userId, "friendId", friendId));
        if (rowSet.next()) {
            userId = rowSet.getLong("user_id");
            friendId = rowSet.getLong("friend_id");
            status = Status.CONFIRMED.toString();
            sql = "UPDATE friendship SET status = ? WHERE (user_id = ? AND friend_id = ?)";
        } else {
            sql = "INSERT INTO friendship(status, user_id, friend_id) VALUES (?, ? ,?)";
            status = Status.UNCONFIRMED.toString();
        }
        jdbcTemplate.update(sql, status, userId, friendId);
    }

    @Override
    public void delFromFriendsList(Long userId, Long friendId) {
        String sql;
        String status;
        String rowSetSql = "SELECT * " +
                "FROM friendship AS f " +
                "WHERE (user_id = :userId AND friend_id = :friendId) " +
                "OR (user_id = :friendId AND friend_id = :userId)";
        SqlRowSet rowSet = nmJdbcTemplate.queryForRowSet(rowSetSql, Map.of("userId", userId, "friendId", friendId));
        rowSet.next();
        if (rowSet.getLong("user_id") == userId
                && Status.CONFIRMED.toString().equals(rowSet.getString("status"))) {
            userId = rowSet.getLong("friend_id");
            friendId = rowSet.getLong("user_id");
            status = Status.UNCONFIRMED.toString();
            sql = "UPDATE friendship SET user_id = :userId, friend_id = :friendId, status = :status " +
                    "WHERE (user_id = :friendId AND friend_id = :userId)";
            //jdbcTemplate.update(sql, userId, friendId, status, friendId, userId);
            nmJdbcTemplate.update(sql, Map.of("userId", userId, "friendId", friendId, "status", status));
        }

        if (rowSet.getLong("user_id") == userId
                && Status.UNCONFIRMED.toString().equals(rowSet.getString("status"))) {
            sql = "DELETE FROM friendship WHERE (user_id = ? AND friend_id = ?)";
            jdbcTemplate.update(sql, userId, friendId);
        }

        if (rowSet.getLong("user_id") == friendId
                && Status.CONFIRMED.toString().equals(rowSet.getString("status"))) {
            userId = rowSet.getLong("user_id");
            friendId = rowSet.getLong("friend_id");
            status = Status.UNCONFIRMED.toString();
            sql = "UPDATE friendship SET user_id = :userId, friend_id = :friendId, status = :status " +
                    "WHERE (user_id = :userId AND friend_id = :friendId)";
            nmJdbcTemplate.update(sql, Map.of("userId", userId, "friendId", friendId, "status", status));
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder().id(rs.getLong("user_id"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .friendsList(makeFriendList(rs.getLong("user_id")))
                .build();
    }

    private Map<Long, Status> makeFriendList(Long user_id) throws SQLException {
        String sql = "SELECT f.friend_id, f.STATUS " +
                "FROM friendship AS f " +
                "WHERE f.user_id = ? AND f.status = 'CONFIRMED' " +
                "UNION " +
                "SELECT f.user_id, f.STATUS " +
                "FROM friendship AS f " +
                "WHERE f.friend_id = ? AND (f.status = 'CONFIRMED' OR f.status = 'UNCONFIRMED') ";
        SqlRowSet res = jdbcTemplate.queryForRowSet(sql, user_id, user_id);
        Map<Long, Status> friendsList = new HashMap<>();
        while (res.next()) {
            friendsList.put(res.getLong("friend_id"), Status.valueOf(res.getString("status")));
        }
        return friendsList;
    }
}

