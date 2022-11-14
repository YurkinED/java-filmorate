package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Data
public class Film {
    private int id;
    @NotBlank(message = "Нужно заполнить название фильма.")
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private long duration;
    @NotNull
    private Mpa mpa;

    private Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));

    private Set<Director> directors = new TreeSet<>(Comparator.comparingLong(Director::getId));

    /* private Set<Integer> likes = new TreeSet<>();*/
    private int rating;

    public Film(int id, String name, String description, LocalDate releaseDate, long duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addDirectorToFilm(Director director) {
        log.info("Метод addDirectorToFilm в фильме запущен {}", director);
        directors.add(director);
        log.info("Режиссеры добавлены в фильм {}", director);
    }

    public void addGenresToFilm(Genre genre) {
        log.info("Метод addDirectorToFilm в фильме запущен {}", genre);
        genres.add(genre);
        log.info("Режиссеры добавлены в фильм {}", genre);
    }

    public int getYear() {
        return releaseDate.getYear();
    }

    public void clearGenres() {
        genres.clear();
    }

    public void clearDirectors() {
        directors.clear();
    }

}
