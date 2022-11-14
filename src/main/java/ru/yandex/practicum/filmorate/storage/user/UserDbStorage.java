package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.platform.commons.util.StringUtils.isBlank;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<User> findAllUsers() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_ALL_USERS, (rs, rowNum) -> Mapper.makeUser(rs));
    }

    @Override
    public User createUser(User user) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = getUserParameters(user);

        namedParameterJdbcTemplate.update(SQL_QUERY_CREATE_USER, parameters, keyHolder, new String[]{"user_id"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(id);
        log.info("Создан пользователь: {} {}", user.getId(), user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();

        if (checkUserExists(userId)) {
            String name = user.getName();
            if (isBlank(name)) {
                user.setName(user.getLogin());
            }

            MapSqlParameterSource parameters = getUserParameters(user);
            parameters.addValue("user_id", userId);
            namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_USER, parameters);
            log.info("Обновлен пользователь: {} {}", user.getId(), user.getName());
            return user;
        } else {
            throw new InvalidIdException("Пользователя с id: " + userId + " не существует.");
        }
    }

    @Override
    public Optional<User> findUserById(int userId) {
        SqlRowSet userRows = namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_FIND_USER_BY_ID, userId);
        if (userRows.next()) {
            User user = Mapper.makeUser(userRows);
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public boolean checkUserExists(int userId) {
        SqlRowSet userRows = namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_USER_EXISTS, userId);
        if (userRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteUserById(int userId) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_USER_BY_ID, userId);
    }



    private MapSqlParameterSource getUserParameters(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", user.getEmail());
        parameters.addValue("login", user.getLogin());
        parameters.addValue("name", user.getName());
        parameters.addValue("birthday", user.getBirthday().format(formatter));
        return parameters;
    }
}

