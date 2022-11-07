package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class DirectorService {
    private final DirectorDbStorage directorDbStorage;

    @Autowired
    public DirectorService(DirectorDbStorage directorDbStorage) {
        this.directorDbStorage = directorDbStorage;
    }

    public Collection<Director> findAllDirectors() {
        return directorDbStorage.findAllDirectors();
    }

    public Optional<Director> findDirectorById(@PathVariable int genreId) {
        return directorDbStorage.findDirectorById(genreId);
    }

    public Director createDirector(Director director) {
        return directorDbStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorDbStorage.updateDirector(director);
    }

    public void removeDirector(int directorId) {
        directorDbStorage.removeDirector(directorId);
    }

}
