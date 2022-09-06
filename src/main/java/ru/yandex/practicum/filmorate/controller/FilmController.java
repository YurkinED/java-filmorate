package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.helpTools.IdCounter;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final FilmValidator filmValidator = new FilmValidator();
    private final IdCounter idCounter = new IdCounter();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET /films. Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films.");
        film.setId(idCounter.incrementIdCounter());
        filmValidator.validator(film);
        int id = film.getId();
        if (!films.containsKey(id)) {
            films.put(id, film);
            return film;
        } else {
            throw new InvalidIdException("Не удалось создать фильм. Фильм с id: " + id + " уже существует." );
        }

    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
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
}
