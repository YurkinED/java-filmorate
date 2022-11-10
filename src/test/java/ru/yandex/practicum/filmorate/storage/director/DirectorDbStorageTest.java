package ru.yandex.practicum.filmorate.storage.director;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorDbStorageTest {

    private final DirectorDbStorage directorDbStorage;

    @Test
    @DisplayName("Тест на поиск всех режиссеров")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllDirectors() {
        Collection<Director> directors = directorDbStorage.findAllDirectors();
        assertTrue(directors.size() == 6);
    }

    @Test
    @DisplayName("Тест на поиск режиссера по id")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testGetDirectorById() {
        Optional<Director> directorOptional = directorDbStorage.findDirectorById(4L);

        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(director ->
                        assertThat(director).hasFieldOrPropertyWithValue("id", 4L));
        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(director ->
                        assertThat(director).hasFieldOrPropertyWithValue("name", "Андрей Звягинцев"));
    }

    @Test
    @DisplayName("Тест на создание режиссера")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateDirector() {
        Director testDirector = new Director(7, "Кристофер Нолан");
        Director directorCreated = directorDbStorage.createDirector(testDirector);

        assertThat(directorCreated).hasFieldOrPropertyWithValue("id", 7L);
    }

    @Test
    @DisplayName("Тест на обновление режиссера")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void updateDirector() {
        Director testDirector = new Director(1, "Кристофер Нолан");
        Director directorCreated = directorDbStorage.updateDirector(testDirector);

        assertThat(directorCreated).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(directorCreated).hasFieldOrPropertyWithValue("name", "Кристофер Нолан");
    }

    @Test
    @DisplayName("Тест на удаление режиссера")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void deleteDirector() {
        Collection<Director> directors = directorDbStorage.findAllDirectors();
        assertTrue(directors.size() == 6);

        directorDbStorage.removeDirector(1);
        Collection<Director> newDirectors = directorDbStorage.findAllDirectors();
        assertTrue(newDirectors.size() == 5);
    }
}
