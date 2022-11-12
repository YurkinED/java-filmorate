package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    Optional<User> findUserById(int userId);
    void deleteUserById(int userId);

    List<User> showUserFriendsId(int userId);

    List<User> showCommonFriends(int userId, int friendId);

    boolean checkFriendshipExists(int userId, int friendId);

    void removeFromFriends(int userId, int friendId);

    void addToFriend(int userId, int friendId);
}
