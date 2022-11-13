package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final static LocalDate BIRTHDAY = LocalDate.of(1967, 3, 25);
    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    @DisplayName("Тест на создание пользователя с корректными данными")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateUserWithFullData() {
        User testUser = new User(1, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        User userCreated = userStorage.createUser(testUser);

        assertThat(userCreated).hasFieldOrPropertyWithValue("id", 5);
    }

    @Test
    @DisplayName("Тест на поиск всех пользователей")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllUsers() {
        Collection<User> users = userStorage.findAllUsers();
        assertEquals(4, users.size());
    }


    @Test
    @DisplayName("Тест на обновление всех пользователя")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testUpdateUser() {
        User testUser = new User(1, "solntmore@gmail.com", RandomString.make(10),
                "rambler", BIRTHDAY);
        User userUpdated = userStorage.updateUser(testUser);

        assertThat(userUpdated).hasFieldOrPropertyWithValue("id", 1);
        assertThat(userUpdated).hasFieldOrPropertyWithValue("name", "rambler");
        assertThat(userUpdated).hasFieldOrPropertyWithValue("login", testUser.getLogin());
    }

    @Test
    @DisplayName("Тест на поиск пользователя по id")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findUserById(2);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 2));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "yandex"));
    }

    @Test
    @DisplayName("Тест на проверку наличия пользователя")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCheckUserExists() {
        boolean trueFlag = userStorage.checkUserExists(1);
        boolean falseFlag = userStorage.checkUserExists(18);

        assertTrue(trueFlag);
        assertFalse(falseFlag);
    }

    @Test
    @DisplayName("Тест на проверку наличия дружбы")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCheckFriendshipExists() {
        boolean trueFlag = friendStorage.checkFriendshipExists(1, 2);
        boolean falseFlag = friendStorage.checkFriendshipExists(1, 4);

        assertTrue(trueFlag);
        assertFalse(falseFlag);
    }

    @Test
    @DisplayName("Тест на добавление в друзья")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testAddToFriend() {
        boolean flag = friendStorage.checkFriendshipExists(1, 4);
        assertFalse(flag);

        friendStorage.addToFriend(1, 4);
        flag = friendStorage.checkFriendshipExists(1, 4);
        assertTrue(flag);
    }

    @Test
    @DisplayName("Тест на поиск друзей по id")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowUserFriendsId() {
        List<User> friendsForUser1 = friendStorage.showUserFriendsId(1);
        List<User> friendsForUser4 = friendStorage.showUserFriendsId(4);

        assertEquals(2, friendsForUser1.size());
        assertEquals(1, friendsForUser4.size());

    }

    @Test
    @DisplayName("Тест на поиск общих друзей")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowCommonFriends() {
        List<User> commonFriends1And2 = friendStorage.showCommonFriends(1, 2);
        List<User> commonFriends2And4 = friendStorage.showCommonFriends(2, 4);

        assertEquals(1, commonFriends1And2.size());
        assertEquals(1, commonFriends2And4.size());
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testDeleteUserByIdCheckAllUsers() {
        List<User> users = userStorage.findAllUsers();
        assertThat(users.size() == 4);
        List<User> usersSecond = userStorage.findAllUsers();
        userStorage.deleteUserById(1);
        assertThat(usersSecond.size() == 3);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testDeleteUserByIdCheckFriends() {
        userStorage.deleteUserById(2);
        List<User> friendsForUser1 = friendStorage.showUserFriendsId(1);
        boolean falseFlag = friendStorage.checkFriendshipExists(1, 2);
        assertEquals(1, friendsForUser1.size());
        assertFalse(falseFlag);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testDeleteUserByIdCheckLikes() {
        userStorage.deleteUserById(2);
        boolean flag = filmDbStorage.checkLikeFilm(1, 2);
        assertFalse(flag);
    }
}
