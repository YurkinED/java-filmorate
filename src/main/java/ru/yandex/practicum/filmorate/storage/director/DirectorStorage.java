package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> findAllDirectors();

    Optional<Director> findDirectorById(long directorId);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(long directorId);

    List<Director> makeDirectorFromArray(String directors) throws SQLException;

}
