package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> findFilmById(int filmId);

    boolean checkLikeFilm(int filmId, int userId);

    void likeFilmOrRemoveLike(int filmId, int userId, boolean flag);

    List<Film> commonLikedFilms(int userId, int friendId);

    void deleteFilmById(int filmId);

    List<Film> showMostLikedFilmsFilter(Integer limit, Integer genreId, Integer year);

    List<Film> getRecommendations(int userId);

    List<Film> searchFilmsByTitle(String query);

    List<Film> searchFilmsByDirector(String query);

    List<Film> searchFilmsByTitleAndDirector(String query);

}
