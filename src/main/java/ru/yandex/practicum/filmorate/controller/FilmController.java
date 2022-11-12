package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.SearchingParts;
import ru.yandex.practicum.filmorate.enums.SortingChoices;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.debug("Получен запрос GET /films. Показать все фильмы");
        return filmService.findAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        log.debug("Получен запрос GET /films/{}. Найти фильм по filmId{} ", filmId, filmId);
        return filmService.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("К сожалению, фильма с id " + filmId + " нет."));
    }

    @GetMapping(value = {"/popular"})
    public List<Film> showMostLikedFilms(@RequestParam(name = "count", required = false, defaultValue = "10")
                                         @Min(1) Integer limit,
                                         @RequestParam(name = "genreId", required = false, defaultValue = "0")
                                         Integer genreId,
                                         @RequestParam(name = "year", required = false, defaultValue = "10000")
                                         @Min(1895) Integer year) {
        log.debug("Получен запрос GET /films/popular?count={}&genreId={}&year={}. Показать топ фильмов по лайкам " +
                "с id жанра {} за {} год.", limit, genreId, year, genreId, year);
        return filmService.showMostLikedFilmsFilter(limit, genreId, year);
    }

    @GetMapping("director/{directorId}")
    public List<Film> showDirectorFilms(@PathVariable int directorId, @RequestParam("sortBy") SortingChoices sortBy ) {
        List<Film> directorFilms;
        switch (sortBy) {
            case YEAR:
                log.debug("Получен запрос GET /films/director/{}?sortBy=[year]. Показать топ фильмов режиссера {} по годам.",
                        directorId, directorId);
                directorFilms = filmService.showDirectorsFilmsAndSort(directorId,
                        SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR);
                break;
            default:
                log.debug("Получен запрос GET /films/director/{}?sortBy=[likes]. Показать топ фильмов режиссера {} " +
                        "по популярности.", directorId, directorId);
                directorFilms = filmService.showDirectorsFilmsAndSort(directorId,
                        SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING);
                break;
        }
        return directorFilms;
    }

    @GetMapping(value = {"/common"})
    public List<Film> showCommonLikedFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.debug("Получен запрос GET common?userId={}&friendId={}. Вывод общих с другом фильмов с сортировкой по " +
                "их популярности..", userId, friendId);
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
        log.debug("Получен запрос DELETE /films/{}. Удалить фильм по filmId{} ", filmId, filmId);
        filmService.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("К сожалению, фильма с id " + filmId + " нет."));
        filmService.deleteFilmById(filmId);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam String query, @RequestParam List<SearchingParts> by) {
        log.debug("Получен запрос GET /films/search. Найти фильм по запросу {} ", query);
        return filmService.searchFilms(query, by);
    }
}
