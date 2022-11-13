package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForUser.SQL_QUERY_SHOW_FEEDS_BY_USER_ID;
@Slf4j
@Repository
@RequiredArgsConstructor
public class FeedDbStorage implements FeedStorage {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Collection<Feed> showUsersFeeds(int id) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query(SQL_QUERY_SHOW_FEEDS_BY_USER_ID,
                (rs, rowNum) -> Mapper.makeFeed(id, rs), id);
    }

    @Override
    public void createFeed(int userId, int entityId, Feed.Event eventType, Feed.Operation operation) {
        LocalDateTime now = LocalDateTime.now();
        String sqlQuery = "insert into feeds(user_id, event_type, operation, entity_id, creation_time) " +
                "values (?, ?, ?, ?, ?)";
        namedParameterJdbcTemplate.getJdbcTemplate().update(sqlQuery,
                userId,
                eventType.name(),
                operation.name(),
                entityId,
                Timestamp.valueOf(now).getTime());
    }

}
