package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.platform.commons.util.StringUtils.isBlank;
import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.*;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public UserDbStorage(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }

    @Override
    public Collection<User> findAllUsers() {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_TAKE_ALL_USERS, (rs, rowNum) -> Mapper.makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        String name = user.getName();

        if (isBlank(name)) {
            user.setName(user.getLogin());
        }
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
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate());

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public boolean checkUserExists(int userId) {
        SqlRowSet likeRows = namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_USER_EXISTS, userId);
        if (likeRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkFriendshipExists(int userId, int friendId) {
        MapSqlParameterSource parameters = makeFriendsParameters(userId, friendId);
        SqlRowSet likeRows = namedParameterJdbcTemplate.queryForRowSet(SQL_QUERY_CHECK_FRIENDSHIP_EXISTS, parameters);
        if (likeRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addToFriend(int userId, int friendId) {
        MapSqlParameterSource parameters = makeFriendsParameters(userId, friendId);
        namedParameterJdbcTemplate.update(SQL_QUERY_ADD_TO_FRIEND, parameters);
    }

    @Override
    public Collection<User> showUserFriendsId(int userId) {
        List<User> friendsList = new ArrayList<>();
        SqlRowSet userRows = namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_TAKE_FRIENDS_BY_USER_ID, userId);

        while (userRows.next()) {
            int friendId = userRows.getInt("second_user_id");

            SqlRowSet friendRows = namedParameterJdbcTemplate.getJdbcTemplate().queryForRowSet(SQL_QUERY_FIND_USER_BY_ID, friendId);
            friendRows.first();
            User user = new User(
                    friendRows.getInt("user_id"),
                    friendRows.getString("email"),
                    friendRows.getString("login"),
                    friendRows.getString("name"),
                    Objects.requireNonNull(friendRows.getDate("birthday")).toLocalDate());

            friendsList.add(user);
            log.info("В список друзей добавлен пользователь: {} {}", user.getId(), user.getName());
        }
        return friendsList;
    }

    @Override
    public Collection<User> showCommonFriends(int userId, int friendId) {
        findUserById(userId)
                .orElseThrow(() -> new InvalidIdException("Нет пользователя с id " + userId));
        findUserById(friendId)
                .orElseThrow(() -> new InvalidIdException("Нет пользователя с id " + friendId));
        return showUserFriendsId(userId).stream()
                .filter(id -> showUserFriendsId(friendId).contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromFriends(int userId, int friendId) {
        MapSqlParameterSource parameters = makeFriendsParameters(userId, friendId);
        namedParameterJdbcTemplate.update(SQL_QUERY_REMOVE_FROM_FRIENDS, parameters);
    }

    private MapSqlParameterSource getUserParameters(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("email", user.getEmail());
        parameters.addValue("login", user.getLogin());
        parameters.addValue("name", user.getName());
        parameters.addValue("birthday", user.getBirthday().format(formatter));
        return parameters;
    }

    private MapSqlParameterSource makeFriendsParameters(int userId, int friendId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("first_user_id", userId);
        parameters.addValue("second_user_id", friendId);
        return parameters;
    }

    @Override
    public void deleteUserById(int userId) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_USER_BY_ID, userId);
    }
}

