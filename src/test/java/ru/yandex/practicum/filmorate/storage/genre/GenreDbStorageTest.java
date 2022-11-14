package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {

    private final GenreDbStorage genreDbStorage;

    @Test
    @DisplayName("Тест на поиск всех жанров")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllGenres() {
        Collection<Genre> genres = genreDbStorage.findAllGenres();
        assertEquals(6, genres.size());
    }

    @Test
    @Deprecated
    @DisplayName("Тест на поиск жанра по id")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindGenreById() {
        Optional<Genre> genreOptional = genreDbStorage.findGenreById(2);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 2L));
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма"));
    }
}
