package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.LikesException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    public void addLikeToFilm(int filmId, int userId) {
        Optional<Film> optionalFilm = inMemoryFilmStorage.findFilmById(filmId);
        if (inMemoryUserStorage.findUserById(userId).isPresent()
                && optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            if (film.addLike(userId)) {
                inMemoryFilmStorage.updateFilmsMap(filmId, film);
            } else {
                throw new LikesException("Вы уже поставили лайк ранее.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        Optional<Film> optionalFilm = inMemoryFilmStorage.findFilmById(filmId);
        if (inMemoryUserStorage.findUserById(userId).isPresent()
                && optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            if (film.removeLike(userId)) {
                inMemoryFilmStorage.updateFilmsMap(filmId, film);
            } else {
                throw new LikesException("Вы уже поставили лайк ранее.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
    }

    public List<Film> showMostLikedFilms(int count) {
        return inMemoryFilmStorage.findAllFilms()
                .stream().sorted(Comparator.comparingLong(Film::showQuantityOfLikes).reversed())
                .limit(count).collect(Collectors.toList());
    }

    public Collection<Film> findAllFilms() {
        return inMemoryFilmStorage.findAllFilms();
    }

    public Optional<Film> findFilmById(int filmId) {
        return inMemoryFilmStorage.findFilmById(filmId);
    }

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }
}
