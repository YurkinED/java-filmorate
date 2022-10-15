package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> findAllGenres();

    Optional<Genre> findGenreById(int genreId);

}
