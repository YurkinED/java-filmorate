package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreStorage {

    List<Genre> findAllGenres();

    List<Genre> makeGenreFromArray(String genres) throws SQLException;

    Optional<Genre> findGenreById(int genreId);

}
