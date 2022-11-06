package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.director.DirectorService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.SQL_QUERY_DELETE_FILM_BY_ID;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final DirectorService directorService;

    @Autowired
    public FilmDbStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate, DirectorService directorService) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.directorService = directorService;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_ALL_FILMS_AND_RATINGS,
                (rs, rowNum) -> makeFilmFromRs(rs));
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = getFilmParameters(film);
        namedParameterJdbcTemplate.update(SQL_QUERY_CREATE_FILM, parameters, keyHolder, new String[]{"film_id"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        return installFilmGenresAndDirectors(film);
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        findFilmById(filmId)
                .orElseThrow(() -> new InvalidIdException("Нет фильма с id " + filmId));
        MapSqlParameterSource parameters = getFilmParameters(film);
        parameters.addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_FILM, parameters);
        return installFilmGenresAndDirectors(film);
    }

    public Optional<Film> findFilmById(int filmId) {
        SqlRowSet filmRows =
                namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_FILM_RATING_AND_MPA_BY_ID,
                        filmId);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration"),
                    new Mpa(
                            filmRows.getInt("mpa_id"),
                            filmRows.getString("mpa_name"))
            );
            film.setRating(filmRows.getInt("rating"));
            return Optional.of(addGenreAndDirectorToFilm(film));
        } else {
            return Optional.empty();
        }
    }

    public boolean checkLikeFilm(int filmId, int userId) {
        SqlRowSet likeRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_CHECK_LIKE, filmId, userId);
        if (likeRows.next()) {
            log.debug("Лайк от пользователя userId {} на фильм {} уже стоит", filmId, userId);
            return true;
        } else {
            log.debug("Лайк от пользователя userId {} на фильм {} не стоит", filmId, userId);
            return false;
        }
    }

    public void likeFilmOrRemoveLike(int filmId, int userId, boolean flag) {
        MapSqlParameterSource parameters = getLikeParameters(filmId, userId);
        if (flag) {
            namedParameterJdbcTemplate.update(SQL_QUERY_LIKE_FILM, parameters);
        } else {
            namedParameterJdbcTemplate.update(SQL_QUERY_DELETE_LIKE, parameters);
        }
    }

    public List<Film> findFilmsByDirectorAndSort(int directorId, String query) {
        directorService.findDirectorById(directorId)
                .orElseThrow(() -> new InvalidIdException("Нет режиссера с id " + directorId));
        ArrayList<Film> films = new ArrayList<>();
        SqlRowSet filmRows =
                namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(query,
                        directorId);
        while (filmRows.next()) {
            films.add(makeFilm(filmRows));
        }
        return films;
    }

    public List<Film> commonLikedFilms(int userId, int friendId) {
        SqlRowSet filmRows =
                namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_COMMON_FILMS, userId, friendId);
        List<Film> returnList = new ArrayList<>();
        while (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration"),
                    new Mpa(
                            filmRows.getInt("mpa_id_in_film"),
                            filmRows.getString("mpa_name"))
            );
            returnList.add(film);
        }
        return returnList;
    }

    public void deleteFilmById(int filmId) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILM_BY_ID, filmId);
    }

    public List<Film> showMostLikedFilmsByYearAndGenre(Optional<Integer> limit, Optional<Integer> genreId,
                                                       Optional<String> year, String query) {
        ArrayList<Film> films = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        if (limit.isPresent())
            parameters.addValue("limit", limit.get());
        if (genreId.isPresent())
            parameters.addValue("genre_id", genreId.get());
        if (year.isPresent())
            parameters.addValue("year", year.get());

        SqlRowSet filmRows = namedParameterJdbcTemplate.queryForRowSet(query, parameters);
        while (filmRows.next()) {
            films.add(makeFilm(filmRows));
        }
        return films;
    }

    private Film makeFilm(SqlRowSet filmRows) {
        int id = filmRows.getInt("film_id");
        String name = filmRows.getString("film_name");
        String description = filmRows.getString("description");
        LocalDate releaseDate = Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate();
        long duration = filmRows.getLong("duration");
        int mpaId = filmRows.getInt("mpa_id");
        String mpaName = filmRows.getString("mpa_name");
        Film film = new Film(id, name, description, releaseDate, duration, new Mpa(mpaId, mpaName));
        film.setRating(filmRows.getInt("rating"));
        return addGenreAndDirectorToFilm(film);
    }

    private Film addGenreAndDirectorToFilm(Film film) {
        SqlRowSet genreRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_FILMS_GENRE_AND_DIRECTOR_BY_ID, film.getId());
        while (genreRows.next()) {
            int genreId = genreRows.getInt("genre_id");
            String genreName = genreRows.getString("genre_name");
            int directorId = genreRows.getInt("director_id");
            String directorName = genreRows.getString("director_name");
            if (genreId != 0 && genreName != null) {
                film.addGenresToFilm(new Genre(genreId, genreName));
            }
            if (directorId != 0 && directorName != null) {
                film.addDirectorToFilm(new Director(directorId, directorName));
            }
        }
        return film;
    }

    private Film installFilmGenresAndDirectors(Film film) {
        int filmId = film.getId();
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILMS_GENRE, filmId);
        MapSqlParameterSource parameters;

        Set<BaseEntity> genres = film.getGenres();
        if (genres.size() > 0) {
            for (BaseEntity element : film.getGenres()) {
                int genreId = element.getId();
                parameters = new MapSqlParameterSource();
                parameters.addValue("film_id", filmId);
                parameters.addValue("genre_id", genreId);
                namedParameterJdbcTemplate.update(SQL_QUERY_INSERT_FILMS_GENRE, parameters);
            }
        }

        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILMS_DIRECTORS, filmId);
        Set<BaseEntity> directors = film.getDirectors();
        if (directors.size() > 0) {
            for (BaseEntity element : film.getDirectors()) {
                int directorId = element.getId();
                parameters = new MapSqlParameterSource();
                parameters.addValue("film_id", filmId);
                parameters.addValue("director_id", directorId);
                namedParameterJdbcTemplate.update(SQL_QUERY_INSERT_FILMS_DIRECTOR, parameters);
            }
        }
        return film;
    }

    private MapSqlParameterSource getFilmParameters(Film film) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("film_name", film.getName());
        parameters.addValue("description", film.getDescription());
        parameters.addValue("release_date", String.valueOf(film.getReleaseDate()));
        parameters.addValue("duration", film.getDuration());
        parameters.addValue("mpa_id", film.getMpa().getId());
        return parameters;
    }

    private MapSqlParameterSource getLikeParameters(int filmId, int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("film_id", filmId);
        parameters.addValue("user_id", userId);
        return parameters;
    }


    private Film makeFilmFromRs(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");
        Film film = new Film(id, name, description, releaseDate, duration, new Mpa(mpaId, mpaName));
        film.setRating(rs.getInt("rating"));
        return addGenreAndDirectorToFilm(film);
    }
}
