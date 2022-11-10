package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;

import java.util.Collection;

@Service
public class FeedService {
    private final FeedStorage feedStorage;

    @Autowired
    public FeedService(FeedStorage feedStorage) {
        this.feedStorage = feedStorage;
    }

    public Collection<Feed> showUsersFeeds(int id) {
        return feedStorage.showUsersFeeds(id);
    }
}
