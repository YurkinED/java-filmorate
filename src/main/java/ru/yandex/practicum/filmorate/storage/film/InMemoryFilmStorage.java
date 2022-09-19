package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.helpTools.FilmIdCounter;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmValidator filmValidator = new FilmValidator();
    private final FilmIdCounter idCounter = new FilmIdCounter();

    public Collection<Film> findAllFilms() {
        log.debug("Получен запрос GET /films. Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    public Optional<Film> findFilmById(int filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    public Film createFilm(@Valid Film film) {
        log.debug("Получен запрос POST /films.");
        filmValidator.validator(film);
        film.setId(idCounter.incrementIdCounter());
        int id = film.getId();
        if (!films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            idCounter.decrementIdCounter();
            throw new InvalidIdException("Не удалось создать фильм. Фильм с id: " + id + " уже существует." );
        }
    }

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
}
