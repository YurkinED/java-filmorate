package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.InvalidNameException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.LongDescriptionException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.NegativeDurationException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.ReleaseDateException;
import ru.yandex.practicum.filmorate.model.BaseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class FilmValidator {
    private final static LocalDate BENCHMARK_FOR_FILM_DATE = LocalDate.of(1895, 12, 28);

    public void validator(Film film) {
        String name = film.getName();
        BaseEntity mpa = film.getMpa();
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Нужно заполнить название фильма.");
        }
        if (film.getDescription().length() > 200) {
            throw new LongDescriptionException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(BENCHMARK_FOR_FILM_DATE)) {
            throw new ReleaseDateException("Доступная дата релиза не может быть раньше " + BENCHMARK_FOR_FILM_DATE);
        }
        if (film.getDuration() <= 0) {
            throw new NegativeDurationException("Продолжительность фильма должна быть положительной.");
        }
        if (mpa == null) {
            throw new RuntimeException("Необходимо добавить MPA.");
        }
    }
}
