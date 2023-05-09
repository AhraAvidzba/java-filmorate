package ru.yandex.practicum.filmorate.dao.Impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.Status;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class DbUserDaoTest {
    private final DbUserDao userStorage;
    private final DbFilmDao filmStorage;

    @BeforeEach
    public void fillDb() {
        FillDB.fillDB(userStorage, filmStorage);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.getAllUsers();
        assertThat(users).asList().hasSize(3);
    }

    @Test
    public void testFindUserById() {
        User user = userStorage.getUserById(1L);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .birthday(LocalDate.of(1990, 2, 10))
                .login("avidzba")
                .name("A")
                .email("avidzva@yandex.ru").build();
        User returnedUser = userStorage.putUser(user);
        assertThat(returnedUser).hasFieldOrPropertyWithValue("login", "avidzba");
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .id(1L)
                .birthday(LocalDate.of(1990, 2, 10))
                .login("akhraa1")
                .name("A")
                .email("avidzva@yandex.ru").build();
        User returnedUser = userStorage.updateUser(user);
        assertThat(returnedUser).hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("login", "akhraa1");
    }

    @Test
    public void testAddFriend() {
        User user = userStorage.getUserById(2L);
        System.out.println(user);
        Assertions.assertEquals(user.getFriendsList().get(3L), Status.UNCONFIRMED);
        userStorage.addToFriendsList(2L, 3L);
        User updatedUser = userStorage.getUserById(2L);
        System.out.println(updatedUser);
        Assertions.assertEquals(updatedUser.getFriendsList().get(3L), Status.CONFIRMED);
    }

    @Test
    public void testDelFriend() {
        User user = userStorage.getUserById(2L);
        Assertions.assertEquals(user.getFriendsList().get(3L), Status.UNCONFIRMED);
        userStorage.delFromFriendsList(3L, 2L);
        User updatedUser = userStorage.getUserById(2L);
        Assertions.assertNull(updatedUser.getFriendsList().get(3L));
    }
}
