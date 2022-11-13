package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.SQL_QUERY_DELETE_FILM_BY_ID;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
        return addGenreAndDirectorToFilm(installFilmGenresAndDirectors(film));
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        findFilmById(filmId)
                .orElseThrow(() -> new InvalidIdException("Нет фильма с id " + filmId));
        MapSqlParameterSource parameters = getFilmParameters(film);
        parameters.addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_FILM, parameters);
        return addGenreAndDirectorToFilm(installFilmGenresAndDirectors(film));
    }

    @Override
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

    @Override
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

    @Override
    public void likeFilmOrRemoveLike(int filmId, int userId, boolean flag) {
        MapSqlParameterSource parameters = getLikeParameters(filmId, userId);
        if (flag) {
            namedParameterJdbcTemplate.update(SQL_QUERY_LIKE_FILM, parameters);
        } else {
            namedParameterJdbcTemplate.update(SQL_QUERY_DELETE_LIKE, parameters);
        }
    }

    @Override
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
                            filmRows.getInt("mpa_id"),
                            filmRows.getString("mpa_name"))
            );
            returnList.add(film);
        }
        return returnList;
    }

    private Film makeFilm(SqlRowSet filmRows) {
        return addGenreAndDirectorToFilm(Mapper.makeFilm(filmRows));
    }

    @Override
    public void deleteFilmById(int filmId) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILM_BY_ID, filmId);
    }

    @Override
    public List<Film> showMostLikedFilmsFilter(Integer limit, Integer genreId, Integer year) {
        ArrayList<Film> films = new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("genre_id", genreId);
        parameters.addValue("year", year);
        parameters.addValue("limit", limit);
        SqlRowSet filmRows = namedParameterJdbcTemplate.queryForRowSet(SQL_QUERY_FIND_FILM_FILTER, parameters);
        while (filmRows.next()) {
            films.add(makeFilm(filmRows));
        }
        return films;
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        return new ArrayList<>(namedParameterJdbcTemplate.getJdbcTemplate().query(
                SQL_QUERY_TAKE_RECOMMENDED_FILMS,
                (rowSet, rowNum) -> makeFilmFromRs(rowSet),
                userId, userId, userId));
    }

    @Override
    public List<Film> searchFilmsByTitle(String query) {
        String queryParam = "%" + query.toLowerCase() + "%";
        return namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_SEARCH_FILMS_BY_TITLE, (rs, rowNum) -> makeFilmFromRs(rs), queryParam);
    }

    @Override
    public List<Film> searchFilmsByDirector(String query) {
        String queryParam = "%" + query.toLowerCase() + "%";
        return namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_SEARCH_FILMS_BY_DIRECTOR, (rs, rowNum) -> makeFilmFromRs(rs), queryParam);
    }

    @Override
    public List<Film> searchFilmsByTitleAndDirector(String query) {
        String queryParam = "%" + query.toLowerCase() + "%";
        return namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_SEARCH_FILMS_BY_TITLE_AND_DIRECTOR, (rs, rowNum) -> makeFilmFromRs(rs), queryParam, queryParam);
    }

    private Film makeFilmFromRs(ResultSet rs) throws SQLException {
        return addGenreAndDirectorToFilm(Mapper.makeFilmFromRs(rs));
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

    private Film installFilmGenresAndDirectors(Film film) {
        int filmId = film.getId();
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILMS_GENRE, filmId);
        MapSqlParameterSource parameters;

        Set<Genre> genres = film.getGenres();
        if (genres.size() > 0) {
            for (Genre element : film.getGenres()) {
                long genreId = element.getId();
                parameters = new MapSqlParameterSource();
                parameters.addValue("film_id", filmId);
                parameters.addValue("genre_id", genreId);
                namedParameterJdbcTemplate.update(SQL_QUERY_INSERT_FILMS_GENRE, parameters);
            }
        }

        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILMS_DIRECTORS, filmId);
        Set<Director> directors = film.getDirectors();
        if (directors.size() > 0) {
            for (Director element : film.getDirectors()) {
                long directorId = element.getId();
                parameters = new MapSqlParameterSource();
                parameters.addValue("film_id", filmId);
                parameters.addValue("director_id", directorId);
                namedParameterJdbcTemplate.update(SQL_QUERY_INSERT_FILMS_DIRECTOR, parameters);
            }
        }

        film.clearGenres();
        film.clearDirectors();
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


}
