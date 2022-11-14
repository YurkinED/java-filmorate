package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.Genre;


import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.SQL_QUERY_TAKE_ALL_GENRES;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.SQL_QUERY_TAKE_GENRE_BY_ID;


@Repository
@Primary
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> findAllGenres() {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_TAKE_ALL_GENRES, (rs, rowNum) -> Mapper.makeGenre(rs));
    }

    @Override
    public Optional<Genre> findGenreById(int genreId) {
        SqlRowSet genreRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_GENRE_BY_ID, genreId);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("genre_name"));
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }


}
