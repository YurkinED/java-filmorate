package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAllGenres() {
        log.debug("Получен запрос Get /genres. Получить все жанры.");
        return genreService.findAllGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getGenreById(@PathVariable int genreId) {
        log.debug("Получен запрос Get /genres/{}. Найти жанр по genreId {}.", genreId, genreId);
        return genreService.findGenreById(genreId).orElseThrow(
                () -> new InvalidIdException("К сожалению, рейтинга с id " + genreId + " нет."));
    }
}
