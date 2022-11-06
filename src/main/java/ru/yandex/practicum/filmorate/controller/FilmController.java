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

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;

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
        log.debug("Получен запрос GET /films/{}. Найти фильм по filmId ", filmId);
        return filmService.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("К сожалению, фильма с id " + filmId + " нет."));
    }

    @GetMapping(value = {"/popular", "/popular?count={limit}",
            "/popular?count={limit}&genreId={genreId}&year={year}"})
    public List<Film> showMostLikedFilms(@RequestParam(name = "count") Optional<Integer> limit,
                                         @RequestParam(name = "genreId") Optional<Integer> genreId,
                                         @RequestParam(name = "year") Optional<String> year) {

        if (genreId.isPresent() && year.isPresent()) {
            log.debug("Получен запрос GET /films/popular?genreId={}&year={}. Показать топ фильмов по лайкам " +
                    "с id жанра {} за {} год.", genreId.get(), year.get(), genreId.get(), year.get());
            return filmService.showMostLikedFilmsByYearAndGenre(limit, genreId, year,
                    SQL_QUERY_FIND_FILM_BY_GENRE_YEAR_AND_SORT_BY_RATING);

        } else if (genreId.isPresent()) {
            log.debug("Получен запрос GET /films/popular?genreId={}. Показать топ фильмов по лайкам с id жанра {}.",
                    genreId.get(), genreId.get());
            return filmService.showMostLikedFilmsByYearAndGenre(limit, genreId, year,
                    SQL_QUERY_FIND_FILM_BY_GENRE_AND_SORT_BY_RATING);

        } else if (year.isPresent()) {
            log.debug("Получен запрос GET /films/popular?year={}. Показать топ фильмов по лайкам с за {} год.",
                    year.get(), year.get());
            return filmService.showMostLikedFilmsByYearAndGenre(limit, genreId, year,
                    SQL_QUERY_FIND_FILM_BY_YEAR_AND_SORT_BY_RATING);

        } else if (limit.isPresent()) {
            log.debug("Получен запрос GET /films/popular?count={}. Показать топ {} фильмов по лайкам.", limit, limit);
            return filmService.showMostLikedFilms(limit.get());

        } else {
            log.debug("Получен запрос GET /films/popular. Показать топ 10 фильмов по лайкам.");
            return filmService.showMostLikedFilms(10);
        }
    }


    @GetMapping("director/{directorId}")
    public List<Film> showDirectorFilms(@PathVariable int directorId, @RequestParam(name = "sortBy") String sortBy) {
        if (sortBy.equals("year")) {
            log.debug("Получен запрос GET /films/director/{}?sortBy=[year]. Показать топ фильмов режиссера {} по годам.",
                    directorId, directorId);
            return filmService.showDirectorsFilmsAndSort(directorId, SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR);
        } else {
            log.debug("Получен запрос GET /films/director/{}?sortBy=[like]. Показать топ фильмов режиссера {} " +
                    "по популярности.", directorId, directorId);
            return filmService.showDirectorsFilmsAndSort(directorId, SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING);
        }
    }

    @GetMapping(value = {"/common"})
    public List<Film> showCommonLikedFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.debug("Получен запрос GET common?userId={}&friendId={}. Вывод общих с другом фильмов с сортировкой по их популярности..", userId, friendId);
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
        log.debug("Получен запрос DELETE /films/{}. Удалить фильм по filmId ", filmId);
        filmService.findFilmById(filmId).orElseThrow(
                () -> new InvalidIdException("К сожалению, фильма с id " + filmId + " нет."));
        filmService.deleteFilmById(filmId);
    }


}
