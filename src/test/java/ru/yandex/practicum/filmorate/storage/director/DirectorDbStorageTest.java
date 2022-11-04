package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
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
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllDirectors() {
        Collection<Director> directors = directorDbStorage.findAllDirectors();
        assertTrue(directors.size() == 6);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testGetDirectorById() {
        Optional<Director> directorOptional = directorDbStorage.findDirectorById(4);

        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(director ->
                        assertThat(director).hasFieldOrPropertyWithValue("id", 4));
        assertThat(directorOptional)
                .isPresent()
                .hasValueSatisfying(director ->
                        assertThat(director).hasFieldOrPropertyWithValue("name", "Андрей Звягинцев"));
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testCreateDirector() {
        Director testDirector = new Director(7, "Кристофер Нолан");
        Director directorCreated = directorDbStorage.createDirector(testDirector);

        assertThat(directorCreated).hasFieldOrPropertyWithValue("id", 7);
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void updateDirector() {
        Director testDirector = new Director(1, "Кристофер Нолан");
        Director directorCreated = directorDbStorage.updateDirector(testDirector);

        assertThat(directorCreated).hasFieldOrPropertyWithValue("id", 1);
        assertThat(directorCreated).hasFieldOrPropertyWithValue("name", "Кристофер Нолан");
    }

    @Test
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void deleteDirector() {
        Collection<Director> directors = directorDbStorage.findAllDirectors();
        assertTrue(directors.size() == 6);

        directorDbStorage.removeDirector(1);
        Collection<Director> newDirectors = directorDbStorage.findAllDirectors();
        assertTrue(newDirectors.size() == 5);
    }
}
