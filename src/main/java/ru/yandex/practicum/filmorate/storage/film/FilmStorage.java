package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    Film createFilm(@Valid Film film);

    Film updateFilm(@Valid Film film);

    Optional<Film> findFilmById(int filmId);

    boolean checkLikeFilm(int filmId, int userId);

    void likeFilmOrRemoveLike(int filmId, int userId, boolean flag);

    List<Film> findFilmsByDirectorAndSort(int directorId, String query);

    List<Film> commonLikedFilms(int userId, int friendId);

    void deleteFilmById(int filmId);

    List<Film> showMostLikedFilmsFilter(Integer limit, Integer genreId, String year);

    Collection<Film> getRecommendations(int userId);

    List<Film> searchFilmsByTitle(String query);

    List<Film> searchFilmsByDirector(String query);

    List<Film> searchFilmsByTitleAndDirector(String query);

}
