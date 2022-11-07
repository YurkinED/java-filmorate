package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Autowired
    private final FilmValidator filmValidator;
    private int id;

    public InMemoryFilmStorage(FilmValidator filmValidator) {
        this.filmValidator = filmValidator;
    }

    @Override
    public Collection<Film> findAllFilms() {
        log.debug("Получен запрос GET /films. Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    public Optional<Film> findFilmById(int filmId) {
        return Optional.ofNullable(films.get(filmId));
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

    public void updateFilmsMap(int id, Film film) {
        films.put(id, film);
    }

    private int incrementIdCounter() {
        return ++id;
    }

    private int decrementIdCounter() {
        return --id;
    }
}

