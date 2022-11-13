package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.SearchingParts;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.BadSearchQueryException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.LikesException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;

    private final DirectorStorage directorStorage;
    private final FilmValidator filmValidator;

    public void addLikeToFilm(int filmId, int userId) {
        userStorage.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("Пользователь с id" + userId + " не найден"));
        filmStorage.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("Фильм с id" + filmId + " не найден"));
        if (!filmStorage.checkLikeFilm(filmId, userId)) {
            filmStorage.likeFilmOrRemoveLike(filmId, userId, true);
            feedStorage.createFeed(userId, filmId, Feed.Event.LIKE, Feed.Operation.ADD);
            log.warn("Добавлена информация в ленту: пользователь id {} поставил лайк фильму {}", userId, filmId);
        } else {
            throw new LikesException("Вы уже поставили лайк ранее.");
        }
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        userStorage.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("Пользователь с id" + userId + " не найден"));
        filmStorage.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("Фильм с id" + userId + " не найден"));
        if (filmStorage.checkLikeFilm(filmId, userId)) {
            filmStorage.likeFilmOrRemoveLike(filmId, userId, false);
            feedStorage.createFeed(userId, filmId, Feed.Event.LIKE, Feed.Operation.REMOVE);
            log.warn("Добавлена информация в ленту: пользователь id {} удалил лайк фильму {}", userId, filmId);
        } else {
            throw new LikesException("Вы еще не поставили лайк.");
        }
    }

    public List<Film> findAllFilms() {
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

    public List<Film> getRecommendations(int userId) {
        return filmStorage.getRecommendations(userId);
    }

    public List<Film> showDirectorsFilmsAndSort(int directorId, String query) {
        return directorStorage.findFilmsByDirectorAndSort(directorId, query);
    }

    public List<Film> showCommonLikedFilms(int userId, int friendId) {
        return filmStorage.commonLikedFilms(userId, friendId);
    }

    public void deleteFilmById(int filmId) {
        filmStorage.deleteFilmById(filmId);
    }

    public List<Film> showMostLikedFilmsFilter(Integer limit, Integer genreId, Integer year) {
        return filmStorage.showMostLikedFilmsFilter(limit, genreId, year);
    }

    public List<Film> searchFilms(String query, List<SearchingParts> by) {
        if (by.contains(SearchingParts.TITLE) && by.contains(SearchingParts.DIRECTOR)) {
            return filmStorage.searchFilmsByTitleAndDirector(query);
        } else if (by.contains(SearchingParts.TITLE) && by.size() == 1) {
            return filmStorage.searchFilmsByTitle(query);
        } else if (by.contains(SearchingParts.DIRECTOR) && by.size() == 1) {
            return filmStorage.searchFilmsByDirector(query);
        } else {
            throw new BadSearchQueryException("Введен неверный поисковый запрос");
        }
    }
}
