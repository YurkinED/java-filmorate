package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
                .query(SQL_QUERY_TAKE_ALL_GENRES, (rs, rowNum) -> makeGenre(rs));
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


    @Override
    public List<Genre> makeGenreFromArray(String genres) throws SQLException {
        List<Genre> genresList=new ArrayList<>();
        try {
            String[] rs = genres.split(";");
            for(String genre:rs){
                genresList.add(new Genre(Integer.parseInt(genre.split("#")[0]), genre.split("#")[1].strip()));
            }
        } catch (Exception ex){

        }
        return genresList;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");
        return new Genre(id, name);
    }
}
