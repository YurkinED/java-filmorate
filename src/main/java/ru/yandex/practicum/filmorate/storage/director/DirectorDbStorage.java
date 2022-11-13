package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.SQL_QUERY_REMOVE_DIRECTOR;

@Component
@Primary
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Collection<Director> findAllDirectors() {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_TAKE_ALL_DIRECTORS, (rs, rowNum) -> Mapper.makeDirector(rs));
    }

    @Override
    public Optional<Director> findDirectorById(long directorId) {
        SqlRowSet genreRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_DIRECTOR_BY_ID, directorId);
        if (genreRows.next()) {
            Director director = new Director(
                    genreRows.getInt("director_id"),
                    genreRows.getString("director_name"));
            return Optional.of(director);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Director createDirector(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("director_name", director.getName());
        namedParameterJdbcTemplate.update(SQL_QUERY_CREATE_DIRECTOR, parameters, keyHolder, new String[]{"director_id"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        director.setId(id);
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        long directorId = director.getId();
        findDirectorById(directorId)
                .orElseThrow(() -> new InvalidIdException("Нет фильма с id " + director));
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("director_name", director.getName());
        namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_DIRECTOR, parameters);
        return director;
    }

    @Override
    public void removeDirector(long directorId) {
        findDirectorById(directorId)
                .orElseThrow(() -> new InvalidIdException("Нет режиссера с id " + directorId));
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("director_id", directorId);
        namedParameterJdbcTemplate.update(SQL_QUERY_REMOVE_DIRECTOR, parameters);
    }

    @Override
    public List<Film> findFilmsByDirectorAndSort(int directorId, String query) {
        findDirectorById(directorId)
                .orElseThrow(() -> new InvalidIdException("Нет режиссера с id " + directorId));
        ArrayList<Film> films = new ArrayList<>();
        SqlRowSet filmRows =
                namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(query,
                        directorId);
        while (filmRows.next()) {
            films.add(addGenreAndDirectorToFilm(Mapper.makeFilm(filmRows)));
        }
        return films;
    }

    private Film addGenreAndDirectorToFilm(Film film) {
        SqlRowSet genreRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_FILMS_GENRE_AND_DIRECTOR_BY_ID, film.getId());
        Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));
        while (genreRows.next()) {
            int genreId = genreRows.getInt("genre_id");
            String genreName = genreRows.getString("genre_name");
            int directorId = genreRows.getInt("director_id");
            String directorName = genreRows.getString("director_name");
            if (genreId != 0 && genreName != null) {
                genres.add(new Genre(genreId, genreName));
            }
            if (directorId != 0 && directorName != null) {
                film.addDirectorToFilm(new Director(directorId, directorName));
            }
        }
        film.setGenres(genres);
        return film;
    }
}
