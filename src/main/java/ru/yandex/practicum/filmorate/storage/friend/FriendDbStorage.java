package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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
    public List<User> showUserFriendsId(int userId) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_FIND_FRIENDS_USER_BY_ID, (rs, rowNum) -> Mapper.makeUser(rs), userId);
    }

    @Override
    public List<User> showCommonFriends(int userId, int friendId) {
        return showUserFriendsId(userId).stream()
                .filter(id -> showUserFriendsId(friendId).contains(id))
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromFriends(int userId, int friendId) {
        MapSqlParameterSource parameters = makeFriendsParameters(userId, friendId);
        namedParameterJdbcTemplate.update(SQL_QUERY_REMOVE_FROM_FRIENDS, parameters);
    }

    private MapSqlParameterSource makeFriendsParameters(int userId, int friendId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("first_user_id", userId);
        parameters.addValue("second_user_id", friendId);
        return parameters;
    }


}
