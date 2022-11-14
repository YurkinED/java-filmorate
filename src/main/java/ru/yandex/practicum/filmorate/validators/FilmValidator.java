package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class FilmValidator {
    private final static LocalDate BENCHMARK_FOR_FILM_DATE = LocalDate.of(1895, 12, 28);

    public void validator(Film film) {
        if (film.getReleaseDate().isBefore(BENCHMARK_FOR_FILM_DATE)) {
            throw new ReleaseDateException("Доступная дата релиза не может быть раньше " + BENCHMARK_FOR_FILM_DATE);
        }

    }
}
