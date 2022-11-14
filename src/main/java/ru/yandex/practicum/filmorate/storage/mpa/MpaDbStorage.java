package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.SQL_QUERY_TAKE_ALL_MPA;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForFilm.SQL_QUERY_TAKE_MPA_BY_ID;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Collection<Mpa> findAllMpa() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_ALL_MPA, (rs, rowNum) -> Mapper.makeMpa(rs));
    }

    @Override
    public Optional<Mpa> findMpaById(int mpaId) {
        SqlRowSet mpaRows = namedParameterJdbcTemplate
                .getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_MPA_BY_ID, mpaId);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("mpa_name"));
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

}
