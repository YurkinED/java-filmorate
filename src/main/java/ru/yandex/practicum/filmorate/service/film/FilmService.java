package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.LikesException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final FilmValidator filmValidator;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, FilmValidator filmValidator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmValidator = filmValidator;
    }


    public void addLikeToFilm(int filmId, int userId) {
        if (userStorage.findUserById(userId).isPresent()
                && filmStorage.findFilmById(filmId).isPresent()) {
            if (!filmStorage.checkLikeFilm(filmId, userId)) {
                filmStorage.likeFilmOrRemoveLike(filmId, userId, true);
            } else {
                throw new LikesException("Вы уже поставили лайк ранее.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        if (userStorage.findUserById(userId).isPresent()
                && filmStorage.findFilmById(filmId).isPresent()) {
            if (filmStorage.checkLikeFilm(filmId, userId)) {
                filmStorage.likeFilmOrRemoveLike(filmId, userId, false);
            } else {
                throw new LikesException("Вы еще не поставили лайк.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
    }

    public List<Film> showMostLikedFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .limit(count).collect(Collectors.toList());
    }


    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Optional<Film> findFilmById(int filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public Film createFilm(Film film) {
        filmValidator.validator(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("updateFilm в сервисе запущен, объект {}", film);
        filmValidator.validator(film);
        log.info("Валидация в сервисе прошла успешно, объект {}", film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getRecommendations(int userId) {
        return filmStorage.getRecommendations(userId);
    }

    public List<Film> showDirectorsFilmsAndSort(int directorId, String query) {
        return filmStorage.findFilmsByDirectorAndSort(directorId, query);
    }

    public List<Film> showCommonLikedFilms(int userId, int friendId) {
        return filmStorage.commonLikedFilms(userId, friendId);
    }

    public void deleteFilmById(int filmId) {
        filmStorage.deleteFilmById(filmId);
    }

    public List<Film> searchFilms(String query, List<String> by) {
        return null;
    }
}
