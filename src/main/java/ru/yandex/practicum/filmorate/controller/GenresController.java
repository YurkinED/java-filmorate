package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenresController {
    private final FilmService filmService;

    public GenresController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Genre> findAllGenres() {
        log.debug("Получен запрос Get /genres. Получить все жанры.");
        return filmService.findAllGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getFilmById(@PathVariable int genreId) {
        log.debug("Получен запрос Get /genres/{}. Найти жанр по genreId {}.", genreId, genreId);
        return filmService.findGenreById(genreId).orElseThrow(
                () -> new InvalidIdException("К сожалению, жанра с id " + genreId + " нет."));
    }
}
