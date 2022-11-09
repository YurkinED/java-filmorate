package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.BadSearchQueryException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.LikesException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.UsualConstants.*;

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
                userStorage.createFeed (userId, filmId,EVENT_TYPE_LIKE,OPERATION_ADD);
                log.warn("Добавлена информация в ленту: пользователь id {} поставил лайк фильму {}", userId, filmId);
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
                userStorage.createFeed (userId, filmId,EVENT_TYPE_LIKE,OPERATION_REMOVE);
                log.warn("Добавлена информация в ленту: пользователь id {} удалил лайк фильму {}", userId, filmId);
            } else {
                throw new LikesException("Вы еще не поставили лайк.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
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

    public List<Film> showMostLikedFilmsFilter(Integer limit, Integer genreId, String year) {
        return filmStorage.showMostLikedFilmsFilter(limit, genreId, year);
}

    public List<Film> searchFilms(String query, List<String> by) {
        if (by.contains("title") && by.contains("director")) {
            return filmStorage.searchFilmsByTitleAndDirector(query);
        } else if (by.contains("title") && by.size() == 1) {
            return filmStorage.searchFilmsByTitle(query);
        } else if (by.contains("director") && by.size() == 1){
            return filmStorage.searchFilmsByDirector(query);
        } else {
            throw new BadSearchQueryException("Введен неверный поисковый запрос");
        }

    }
}