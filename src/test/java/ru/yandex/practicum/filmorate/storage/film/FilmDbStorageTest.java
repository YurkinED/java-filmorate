package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR;
import static org.junit.jupiter.api.Assertions.assertFalse;


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
    @DisplayName("Тест на поиск всех фильмов")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllFilms() {
        Collection<Film> films = filmStorage.findAllFilms();
        assertEquals(5, films.size());
    }

   /* @Test
    @DisplayName("Тест на создание фильма")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateFilm() {
        Film testFilm = new Film(1, "name", RandomString.make(200), TEST_DATE,
                FILM_DURATION, MPA);
        Film filmCreated = filmStorage.createFilm(testFilm);

        assertThat(filmCreated).hasFieldOrPropertyWithValue("id", 6);
    }

    @Test
    @DisplayName("Тест на обновление фильма")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testUpdateFilm() {
        Film testFilm = new Film(1, "name", RandomString.make(200), TEST_DATE,
                FILM_DURATION, MPA);
        Film filmCreated = filmStorage.updateFilm(testFilm);

        assertThat(filmCreated).hasFieldOrPropertyWithValue("id", 1);
        assertThat(filmCreated).hasFieldOrPropertyWithValue("name", "name");
        assertThat(filmCreated).hasFieldOrPropertyWithValue("duration", testFilm.getDuration());
    }
*/
    @Test
    @DisplayName("Тест на поиск фильма по id")
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
    @DisplayName("Тест на проверку наличия лайка")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCheckLikeFilm() {
        boolean trueFlag = filmStorage.checkLikeFilm(1, 2);
        boolean falseFlag = filmStorage.checkLikeFilm(1, 4);

        assertEquals(true, trueFlag);
        assertEquals(false, falseFlag);
    }

    @Test
    @DisplayName("Тест на добавление и удаление лайков")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testLikeFilmOrRemoveLike() {
        boolean flag = filmStorage.checkLikeFilm(1, 4);
        assertEquals(false, flag);

        filmStorage.likeFilmOrRemoveLike(1, 4, true);
        flag = filmStorage.checkLikeFilm(1, 4);
        assertEquals(true, flag);

        filmStorage.likeFilmOrRemoveLike(1, 4, false);
        flag = filmStorage.checkLikeFilm(1, 4);
        assertEquals(false, flag);
    }

    @Test
    @DisplayName("Тест на поиск фильмов по id режиссера и сортировку по рейтингу")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowDirectorsFilmsSortLikes() {
        List<Film> filmsFirstDirector = filmService
                .showDirectorsFilmsAndSort(1, SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING);
        List<Film> filmsSecondDirector = filmService
                .showDirectorsFilmsAndSort(2, SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING);
        assertEquals(3, filmsFirstDirector.size());
        assertEquals(2, filmsSecondDirector.size());

        Film film = filmsFirstDirector.get(0);
        assertEquals(1, film.getId());
    }

    @Test
    @DisplayName("Тест на поиск фильмов по id режиссера и сортировку по году выпуска")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testShowDirectorsFilmsSortYears() {
        List<Film> filmsFirstDirector = filmService
                .showDirectorsFilmsAndSort(1, SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR);
        List<Film> filmsSecondDirector = filmService
                .showDirectorsFilmsAndSort(2, SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR);
        assertEquals(3, filmsFirstDirector.size());
        assertEquals(2, filmsSecondDirector.size());

        Film film = filmsFirstDirector.get(0);
        assertEquals(2015, film.getYear());
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testDeleteFilmByIdCheckAllFilms() {
        Collection<Film> films = filmStorage.findAllFilms();
        assertEquals(5, films.size());
        filmStorage.deleteFilmById(1);
        Collection<Film> filmsSecond = filmStorage.findAllFilms();
        assertEquals(4, filmsSecond.size());
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testDeleteFilmByIdCheckLikes() {
        filmStorage.deleteFilmById(1);
        boolean flag = filmStorage.checkLikeFilm(1, 2);
        assertFalse(flag);
    }

}
