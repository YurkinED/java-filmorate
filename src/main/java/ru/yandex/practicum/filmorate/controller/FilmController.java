package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.debug("Получен запрос GET /films. Показать все фильмы");
        return filmService.findAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        log.debug("Получен запрос GET /films/{}. Найти фильм по filmId " + filmId);
        return filmService.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("К сожалению, фильма с id " + filmId + " нет."));
    }

    @GetMapping(value = {"/popular", "/popular?count={count}"})
    public List<Film> showMostLikedFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            log.debug("Получен запрос GET /films/popular?count={}. Показать топ {} фильмов по лайкам.", count, count);
            return filmService.showMostLikedFilms(count.get());
        } else {
            log.debug("Получен запрос GET /films/popular. Показать топ 10 фильмов по лайкам.");
            return filmService.showMostLikedFilms(10);
        }
    }

    @GetMapping(value = {"/common"})
    public List<Film> showCommonLikedFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.debug("Получен запрос GET common?userId={}&friendId={}. Вывод общих с другом фильмов с сортировкой по их популярности..",userId, friendId);
        return filmService.showCommonLikedFilms(userId, friendId);
    }


    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        log.debug("Получен запрос Post /films. Создать фильм {}", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.debug("Получен запрос Put /films. Обновить данные фильма {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Получен запрос Put /films/{}/like/{}. Поставить лайк фильму.", filmId, userId);
        filmService.addLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Получен запрос Delete /films/{}/like/{}. Удалить лайк фильму.", filmId, userId);
        filmService.removeLikeFromFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable int filmId) {
        log.debug("Получен запрос DELETE /films/{}. Удалить фильм по filmId " + filmId);
        filmService.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("К сожалению, фильма с id " + filmId + " нет."));
        filmService.deleteFilmById(filmId);
    }


}
