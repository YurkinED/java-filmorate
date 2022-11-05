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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FilmDbStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_ALL_FILMS_AND_RATINGS, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = getFilmParameters(film);
        namedParameterJdbcTemplate.update(SQL_QUERY_CREATE_FILM, parameters, keyHolder, new String[]{"film_id"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        return installFilmGenres(film);
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        findFilmById(filmId)
                .orElseThrow(() -> new InvalidIdException("Нет фильма с id " + filmId));
        MapSqlParameterSource parameters = getFilmParameters(film);
        parameters.addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_FILM, parameters);
        return installFilmGenres(film);
    }

    public Optional<Film> findFilmById(int filmId) {
        SqlRowSet filmRows =
                namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_FILM_AND_MPA_BY_ID, filmId);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate(),
                    filmRows.getLong("duration"),
                    new Mpa(
                            filmRows.getInt("mpa_id_in_mpa"),
                            filmRows.getString("mpa_name"))
            );
            return Optional.of(setLikes(addGenreToFilm(film)));
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

    private Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long duration = rs.getLong("duration");
        int mpaId = rs.getInt("mpa_id_in_film");
        String mpaName = rs.getString("mpa_name");

        Film film = new Film(id, name, description, releaseDate, duration, new Mpa(mpaId, mpaName));
        return setLikes(addGenreToFilm(film));
    }

    private Film setLikes(Film film) {
        SqlRowSet likeRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_COUNT_LIKES, film.getId());
        if (likeRows.next()) {
            int likes = likeRows.getInt("likes");
            film.setRating(likes);
        }
        return film;
    }

    private Film addGenreToFilm(Film film) {
        SqlRowSet genreRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_FILMS_GENRE_BY_ID, film.getId());
        while (genreRows.next()) {
            int genreId = genreRows.getInt("genre_id");
            String genreName = genreRows.getString("genre_name");
            if (genreId != 0 && genreName != null) {
                film.addGenresToFilm(new Genre(genreId, genreName));
            }
        }
        return film;
    }

    private Film installFilmGenres(Film film) {
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
        return film;
    }

    private MapSqlParameterSource getFilmParameters(Film film) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("film_name", film.getName());
        parameters.addValue("description", film.getDescription());
        parameters.addValue("release_date", String.valueOf(film.getReleaseDate()));
        parameters.addValue("duration", film.getDuration());
        parameters.addValue("mpa_id_in_film", film.getMpa().getId());
        return parameters;
    }

    private MapSqlParameterSource getLikeParameters(int filmId, int userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("film_id", filmId);
        parameters.addValue("user_id", userId);
        return parameters;
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
}
