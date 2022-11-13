package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.*;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.SQL_QUERY_DELETE_FILM_BY_ID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Film> findAllFilms() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_ALL_FILMS_AND_RATINGS,
                (rs, rowNum) -> Mapper.makeFilmFromRs(rs));
    }

    @Override
    public Optional<Film> findFilmById(int filmId) {
        List<Film> films=namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_FILM_RATING_AND_MPA_BY_ID, (rs, rowNum) -> Mapper.makeFilmFromRs(rs), filmId);
        System.out.println(SQL_QUERY_TAKE_FILM_RATING_AND_MPA_BY_ID);
        if (films.size()==1){
            return Optional.of(films.get(0));
        }
            return Optional.empty();
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = getFilmParameters(film);
        namedParameterJdbcTemplate.update(SQL_QUERY_CREATE_FILM, parameters, keyHolder, new String[]{"film_id"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(id);
        log.error("Film created");
        saveFilmGenres(film);
        saveFilmDirectors(film);
        log.error("Genres added created");
        return findFilmById(film.getId()).orElseThrow();
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        findFilmById(filmId)
                .orElseThrow(() -> new InvalidIdException("Нет фильма с id " + filmId));
        MapSqlParameterSource parameters = getFilmParameters(film);
        parameters.addValue("film_id", filmId);
        namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_FILM, parameters);
        saveFilmGenres(film);
        saveFilmDirectors(film);
        return findFilmById(film.getId()).orElseThrow();
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
        return new ArrayList<>(namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_FIND_FILM_FILTER, (rs, rowNum) -> Mapper.makeFilmFromRs(rs), genreId, year, limit));
    }

    @Override
    public List<Film> getRecommendations(int userId) {
        return new ArrayList<>(namedParameterJdbcTemplate.getJdbcTemplate().query(
                SQL_QUERY_TAKE_RECOMMENDED_FILMS,
                (rowSet, rowNum) -> Mapper.makeFilmFromRs(rowSet),
                userId, userId, userId));
    }

    @Override
    public List<Film> searchFilmsByTitle(String query) {
        String queryParam = "%" + query.toLowerCase() + "%";
        return namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_SEARCH_FILMS_BY_TITLE, (rs, rowNum) -> Mapper.makeFilmFromRs(rs), queryParam);
    }

    @Override
    public List<Film> searchFilmsByDirector(String query) {
        String queryParam = "%" + query.toLowerCase() + "%";
        return namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_SEARCH_FILMS_BY_DIRECTOR, (rs, rowNum) -> Mapper.makeFilmFromRs(rs), queryParam);
    }

    @Override
    public List<Film> searchFilmsByTitleAndDirector(String query) {
        String queryParam = "%" + query.toLowerCase() + "%";
        return namedParameterJdbcTemplate.getJdbcTemplate().
                query(SQL_QUERY_SEARCH_FILMS_BY_TITLE_AND_DIRECTOR, (rs, rowNum) -> Mapper.makeFilmFromRs(rs), queryParam, queryParam);
    }


    private void saveFilmGenres(Film film) {
        int filmId = film.getId();
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILMS_GENRE, filmId);
        Set<Genre> genres = film.getGenres();
        if (genres.size() > 0) {
            Map<String, Object>[] batchOfInputs = new HashMap[film.getGenres().size()];
            int count = 0;
            for (Genre genre : film.getGenres()) {
                Map<String, Object> map = new HashMap();
                map.put("film_id", filmId);
                map.put("genre_id", genre.getId());
                batchOfInputs[count++] = map;
                System.out.println(batchOfInputs);
            }
            namedParameterJdbcTemplate.batchUpdate(SQL_QUERY_INSERT_FILMS_GENRE, batchOfInputs);
        }

    }

    private void saveFilmDirectors(Film film) {
        int filmId = film.getId();
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_FILMS_DIRECTORS, filmId);
        Set<Director> directors = film.getDirectors();
        if (directors.size() > 0) {
            Map<String,Object>[] batchOfInputs = new HashMap[film.getDirectors().size()];
            int count = 0;
            for(Director director : film.getDirectors()){
                Map<String,Object> map = new HashMap();
                map.put("film_id", filmId);
                map.put("director_id", director.getId());
                batchOfInputs[count++]= map;
                System.out.println(batchOfInputs);
            }
            namedParameterJdbcTemplate.batchUpdate(SQL_QUERY_INSERT_FILMS_DIRECTOR, batchOfInputs);
        }
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
