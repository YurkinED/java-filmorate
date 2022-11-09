package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

public interface FeedStorage {
    void createFeed(int userId, int entityId, Feed.Event eventType, Feed.Operation operation);
    Collection<Feed> showUsersFeeds(int id);
}
