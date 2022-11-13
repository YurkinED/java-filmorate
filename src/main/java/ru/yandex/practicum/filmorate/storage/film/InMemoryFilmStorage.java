package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Component
@Deprecated
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private final FilmValidator filmValidator;
    private int id;

    @Autowired
    public InMemoryFilmStorage(FilmValidator filmValidator) {
        this.filmValidator = filmValidator;
    }

    @Override
    public List<Film> findAllFilms() {
        log.debug("Получен запрос GET /films. Текущее количество фильмов: {}", films.size());
        return new ArrayList<Film>(films.values());
}

    public Optional<Film> findFilmById(int filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public boolean checkLikeFilm(int filmId, int userId) {
        return false;
    }

    @Override
    public void likeFilmOrRemoveLike(int filmId, int userId, boolean flag) {

    }

    @Override
    public List<Film> commonLikedFilms(int userId, int friendId) {
        return null;
    }

    @Override
    public void deleteFilmById(int filmId) {

    }

    @Override
    public List<Film> showMostLikedFilmsFilter(Integer limit, Integer genreId, Integer year) {
        return null;
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        return null;
    }

    @Override
    public List<Film> searchFilmsByTitle(String query) {
        return null;
    }

    @Override
    public List<Film> searchFilmsByDirector(String query) {
        return null;
    }

    @Override
    public List<Film> searchFilmsByTitleAndDirector(String query) {
        return null;
    }

    @Override
    public Film createFilm(@Valid Film film) {
        log.debug("Получен запрос POST /films.");
        filmValidator.validator(film);
        film.setId(incrementIdCounter());
        int id = film.getId();
        if (!films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            decrementIdCounter();
            throw new InvalidIdException("Не удалось создать фильм. Фильм с id: " + id + " уже существует.");
        }
    }

    @Override
    public Film updateFilm(@Valid Film film) {
        log.debug("Получен запрос PUT /films.");
        filmValidator.validator(film);
        int id = film.getId();
        if (films.containsKey(film.getId())) {
            films.put(id, film);
            return film;
        } else {
            throw new InvalidIdException("Не удалось обновить фильм. Нет фильма с id: " + id);
        }
    }

    private int incrementIdCounter() {
        return ++id;
    }

    private int decrementIdCounter() {
        return --id;
    }
}

