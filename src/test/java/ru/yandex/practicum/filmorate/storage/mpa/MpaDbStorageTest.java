package ru.yandex.practicum.filmorate.storage.mpa;

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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;
    @Test
    @DisplayName("Тест на поиск всех mpa")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindAllMpa() {
        Collection<Mpa> mpas = mpaDbStorage.findAllMpa();
        assertEquals(5, mpas.size());
    }

    @Test
    @Deprecated
    @DisplayName("Тест на поиск mpa по id")
    @Sql(scripts = {"file:src/main/resources/setupForTest.sql"})
    public void testFindMpaById() {
        Optional<Mpa> mpaOptional = mpaDbStorage.findMpaById(3);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 3L));
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13"));
    }
}
