package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

public interface DirectorInterface {

    Collection<Director> findAllDirectors();

    Optional<Director> findDirectorById(long directorId);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(long directorId);

}
