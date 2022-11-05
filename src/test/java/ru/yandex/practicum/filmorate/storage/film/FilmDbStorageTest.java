package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final static LocalDate TEST_DATE = LocalDate.of(1895, 12, 29);
    private final static long FILM_DURATION = 200;
    private final static BaseEntity MPA = new BaseEntity(1);
    private final FilmDbStorage filmStorage;

    private final FilmService filmService;

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllFilms() {
        Collection<Film> films = filmStorage.findAllFilms();
        assertTrue(films.size() == 5);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateFilm() {
        Film testFilm = new Film(1, "name", RandomString.make(200), TEST_DATE,
                FILM_DURATION, MPA);
        Film filmCreated = filmStorage.createFilm(testFilm);

        assertThat(filmCreated).hasFieldOrPropertyWithValue("id", 6);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testUpdateFilm() {
        Film testFilm = new Film(1, "name", RandomString.make(200), TEST_DATE,
                FILM_DURATION, MPA);
        Film filmCreated = filmStorage.updateFilm(testFilm);

        assertThat(filmCreated).hasFieldOrPropertyWithValue("id", 1);
        assertThat(filmCreated).hasFieldOrPropertyWithValue("name", "name");
        assertThat(filmCreated).hasFieldOrPropertyWithValue("duration", testFilm.getDuration());
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

        assertTrue(trueFlag == true);
        assertTrue(falseFlag == false);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testLikeFilmOrRemoveLike() {
        boolean flag = filmStorage.checkLikeFilm(1, 4);
        assertTrue(flag == false);

        filmStorage.likeFilmOrRemoveLike(1, 4, true);
        flag = filmStorage.checkLikeFilm(1, 4);
        assertTrue(flag == true);

        filmStorage.likeFilmOrRemoveLike(1, 4, false);
        flag = filmStorage.checkLikeFilm(1, 4);
        assertTrue(flag == false);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowDirectorsFilmsSortLikes() {
        List<Film> filmsFirstDirector = filmService.showDirectorsFilmsSortLikes(1);
        List<Film> filmsSecondDirector = filmService.showDirectorsFilmsSortLikes(2);
        assertTrue(filmsFirstDirector.size() == 3);
        assertTrue(filmsSecondDirector.size() == 2);

        Film film = filmsFirstDirector.get(0);
        assertTrue(film.getId() == 1);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowDirectorsFilmsSortYears() {
        List<Film> filmsFirstDirector = filmService.showDirectorsFilmsSortYear(1);
        List<Film> filmsSecondDirector = filmService.showDirectorsFilmsSortYear(2);
        assertTrue(filmsFirstDirector.size() == 3);
        assertTrue(filmsSecondDirector.size() == 2);

        Film film = filmsFirstDirector.get(0);
        assertTrue(film.getYear() == 2015);
    }


}
