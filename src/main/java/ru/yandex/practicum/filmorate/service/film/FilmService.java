package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.LikesException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }


    public void addLikeToFilm(int filmId, int userId) {
        Optional<Film> optionalFilm = inMemoryFilmStorage.findFilmById(filmId);
        if (inMemoryUserStorage.findUserById(userId).isPresent()
                && optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            if (film.addLike(userId)) {
                inMemoryFilmStorage.updateFilmsMap(filmId, film);
            } else {
                throw new LikesException("Вы уже поставили лайк ранее.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
    }

    public void removeLikeFromFilm(int filmId, int userId) {
        Optional<Film> optionalFilm = inMemoryFilmStorage.findFilmById(filmId);
        if (inMemoryUserStorage.findUserById(userId).isPresent()
                && optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            if (film.removeLike(userId)) {
                inMemoryFilmStorage.updateFilmsMap(filmId, film);
            } else {
                throw new LikesException("Вы уже поставили лайк ранее.");
            }
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или фильма.");
        }
    }

    public ArrayList<Film> showMostLikedFilms(int count) {
        Film[] film = inMemoryFilmStorage.findAllFilms().toArray(new Film[0]);
        ArrayList<Film> films = new ArrayList<>();
        films.addAll(List.of(film));
        films.sort(Comparator.comparingInt(Film::showQuantityOfLikes));
        Collections.reverse(films);
        int k = films.size();
        if (k > count)
            films.subList(count, k).clear();
        return films;
    }
}
