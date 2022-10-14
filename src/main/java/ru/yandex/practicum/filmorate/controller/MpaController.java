package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final FilmService filmService;

    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Mpa> findAllMpa() {
        log.debug("Получен запрос Get /mpa. Получить все рейтинги.");
        return filmService.findAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa getFilmById(@PathVariable int mpaId) {
        log.debug("Получен запрос Get /mpa/{}. Найти жанр по mpaId {}.", mpaId, mpaId);
        return filmService.findMpaById(mpaId).orElseThrow(
                () -> new InvalidIdException("К сожалению, рейтинга с id " + mpaId + " нет."));
    }
}
