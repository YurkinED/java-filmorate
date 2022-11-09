package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    Film createFilm(@Valid Film film);

    Film updateFilm(@Valid Film film);

    Optional<Film> findFilmById(int filmId);

}
