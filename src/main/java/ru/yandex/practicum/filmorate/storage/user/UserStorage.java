package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAllUsers();

    User createUser(@Valid User user);

    User updateUser(@Valid User user);

    Optional<User> findUserById(int userId);
    void deleteUserById(int userId);

    Collection<User> showUserFriendsId(int userId);

    Collection<User> showCommonFriends(int userId, int friendId);

    boolean checkFriendshipExists(int userId, int friendId);

    void removeFromFriends(int userId, int friendId);

    void addToFriend(int userId, int friendId);
}
