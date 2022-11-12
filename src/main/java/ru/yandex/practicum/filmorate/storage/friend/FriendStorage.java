package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage {
    Collection<User> showUserFriendsId(int userId);

    Collection<User> showCommonFriends(int userId, int friendId);

    boolean checkFriendshipExists(int userId, int friendId);

    void removeFromFriends(int userId, int friendId);

    void addToFriend(int userId, int friendId);
}
