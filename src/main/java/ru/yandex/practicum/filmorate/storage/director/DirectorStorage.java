package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> findAllDirectors();

    Optional<Director> findDirectorById(long directorId);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(long directorId);

    List<Film> findFilmsByDirectorAndSort(int directorId, String query);
}
