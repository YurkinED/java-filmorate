package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final static LocalDate BIRTHDAY = LocalDate.of(1967, 03, 25);
    private final static LocalDate TEST_DATE = LocalDate.of(1895, 12, 29);
    private final static long FILM_DURATION = 200;
    private final static BaseEntity mpa = new BaseEntity(1);

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateUserWithFullData() {
        User testUser = new User(1, "solntmore@yandex.ru", RandomString.make(10),
                RandomString.make(10), BIRTHDAY);
        User userCreated = userStorage.createUser(testUser);

        assertThat(userCreated).hasFieldOrPropertyWithValue("id", 5);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateUserWithEmptyName() {
        User testUser = new User(1, "solntmore@gmail.com", RandomString.make(10),
                "", BIRTHDAY);
        User userCreated = userStorage.createUser(testUser);

        assertThat(userCreated).hasFieldOrPropertyWithValue("id", 5);
        assertThat(userCreated).hasFieldOrPropertyWithValue("name", userCreated.getLogin());
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllUsers() {
        Collection<User> users = userStorage.findAllUsers();
        assertThat(users.size() == 4);
    }


    @Test
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
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCheckUserExists() {
        boolean trueFlag = userStorage.checkUserExists(1);
        boolean falseFlag = userStorage.checkUserExists(18);

        assertThat(trueFlag == true);
        assertThat(falseFlag == false);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCheckFriendshipExists() {
        boolean trueFlag = userStorage.checkFriendshipExists(1, 2);
        boolean falseFlag = userStorage.checkFriendshipExists(1, 4);

        assertThat(trueFlag == true);
        assertThat(falseFlag == false);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testAddToFriend() {
        boolean flag = userStorage.checkFriendshipExists(1, 4);
        assertThat(flag == false);

        userStorage.addToFriend(1, 4);
        flag = userStorage.checkFriendshipExists(1, 4);
        assertThat(flag == true);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowUserFriendsId() {
        Collection<User> friendsForUser1 = userStorage.showUserFriendsId(1);
        Collection<User> friendsForUser4 = userStorage.showUserFriendsId(4);

        assertThat(friendsForUser1.size() == 2);
        assertThat(friendsForUser4.size() == 1);

    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowCommonFriends() {
        Collection<User> commonFriends1And2 = userStorage.showCommonFriends(1, 2);
        Collection<User> commonFriends2And4 = userStorage.showCommonFriends(2, 4);

        assertThat(commonFriends1And2.size() == 1);
        assertThat(commonFriends2And4.size() == 1);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllFilms() {
        Collection<Film> films = filmStorage.findAllFilms();
        assertThat(films.size() == 5);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateFilm() {
        Film testFilm = new Film(1, "name", RandomString.make(200), TEST_DATE,
                FILM_DURATION, mpa);
        Film filmCreated = filmStorage.createFilm(testFilm);

        assertThat(filmCreated).hasFieldOrPropertyWithValue("id", 6);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testUpdateFilm() {
        Film testFilm = new Film(1, "name", RandomString.make(200), TEST_DATE,
                FILM_DURATION, mpa);
        Film filmCreated = filmStorage.updateFilm(testFilm);

        assertThat(filmCreated).hasFieldOrPropertyWithValue("id", 1);
        assertThat(filmCreated).hasFieldOrPropertyWithValue("name", "name");
        assertThat(filmCreated).hasFieldOrPropertyWithValue("duration", testFilm.getDuration());
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllGenres() {
        Collection<Genre> genres = filmStorage.findAllGenres();
        assertThat(genres.size() == 6);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindGenreById() {
        Optional<Genre> genreOptional = filmStorage.findGenreById(2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 2));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма"));
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllMpa() {
        Collection<Mpa> mpas = filmStorage.findAllMpa();
        assertThat(mpas.size() == 5);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindMpaById() {
        Optional<Mpa> mpaOptional = filmStorage.findMpaById(3);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 3));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13"));
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmStorage.findFilmById(4);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 4));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Флеш"));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("description", "Не Марвел"));
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCheckLikeFilm() {
        boolean trueFlag = filmStorage.checkLikeFilm(1, 2);
        boolean falseFlag = filmStorage.checkLikeFilm(1, 4);

        assertThat(trueFlag == true);
        assertThat(falseFlag == false);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testLikeFilmOrRemoveLike() {
        boolean flag = filmStorage.checkLikeFilm(1, 4);
        assertThat(flag == false);

        filmStorage.likeFilmOrRemoveLike(1, 4, true);
        flag = filmStorage.checkLikeFilm(1, 4);
        assertThat(flag == true);

        filmStorage.likeFilmOrRemoveLike(1, 4, false);
        flag = filmStorage.checkLikeFilm(1, 4);
        assertThat(flag == false);
        ;
    }

}
