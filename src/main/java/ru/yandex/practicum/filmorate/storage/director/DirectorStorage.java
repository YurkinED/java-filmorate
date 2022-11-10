package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

public interface DirectorStorage {

    Collection<Director> findAllDirectors();

    Optional<Director> findDirectorById(int directorId);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(int directorId);

}
