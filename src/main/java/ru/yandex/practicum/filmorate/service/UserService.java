package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

import static org.junit.platform.commons.util.StringUtils.isBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;

    private final FriendStorage friendStorage;

    public void addToFriends(int userId, int friendId) {
        Optional<User> optionalUser = userStorage.findUserById(userId);
        Optional<User> optionalFriend = userStorage.findUserById(friendId);
        if (optionalUser.isPresent()
                && optionalFriend.isPresent()) {
            if (!friendStorage.checkFriendshipExists(userId, friendId)) {
                friendStorage.addToFriend(userId, friendId);
                log.warn("Пользователь {} и {} стали друзьями", userId, friendId);
                feedStorage.createFeed (userId, friendId, Feed.Event.FRIEND,Feed.Operation.ADD);
                log.warn("Добавлена информация в ленту: пользователь {} и {} стали друзьями", userId, friendId);
            } else {
                log.warn("Пользователь {} и {} уже друзья", userId, friendId);
                throw new InvalidIdException("Пользователи уже являются друзьями, попробуйте другой id.");
            }
        } else {
            log.warn("Неверно введен id {} или {}.", userId, friendId);
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public void removeFromFriends(int userId, int friendId) {
        Optional<User> optionalUser = userStorage.findUserById(userId);
        Optional<User> optionalFriend = userStorage.findUserById(friendId);
        if (optionalUser.isPresent()
                && optionalFriend.isPresent()) {
            if (friendStorage.checkFriendshipExists(userId, friendId)) {
                friendStorage.removeFromFriends(userId, friendId);
                log.warn("Пользователь {} и {} перестали быть друзьями", userId, friendId);
                feedStorage.createFeed (userId, friendId,Feed.Event.FRIEND,Feed.Operation.REMOVE);
                log.warn("Добавлена информация в ленту: пользователь {} и {} перестали быть друзьями", userId, friendId);
            } else {
                log.warn("Пользователь {} и {} не друзья ", userId, friendId);
                throw new InvalidIdException("Пользователи не являются друзьями, попробуйте другой id.");
            }
        } else {
            log.warn("Неверно введен id {} или {}.", userId, friendId);
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public Collection<User> showCommonFriends(int userId, int friendId) {
        findUserById(userId)
                .orElseThrow(() -> new InvalidIdException("Нет пользователя с id " + userId));
        findUserById(friendId)
                .orElseThrow(() -> new InvalidIdException("Нет пользователя с id " + friendId));
        return friendStorage.showCommonFriends(userId, friendId);
    }

    public Collection<User> showUserFriends(int userId) {
        return friendStorage.showUserFriendsId(userId);
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public Optional<User> findUserById(int userId) {
        return userStorage.findUserById(userId);
    }

    public User createUser(User user) {
        String name = user.getName();

        if (isBlank(name)) {
            user.setName(user.getLogin());
        }

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUserById(int userId) {
        userStorage.deleteUserById(userId);
    }
}
