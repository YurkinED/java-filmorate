package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.*;

import static ru.yandex.practicum.filmorate.constants.UsualConstants.*;

@Slf4j
@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    private final UserValidator userValidator;
    private final FeedStorage feedStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage, UserValidator userValidator, FeedStorage feedStorage) {
        this.userDbStorage = userDbStorage;
        this.userValidator = userValidator;
        this.feedStorage = feedStorage;
    }

    public void addToFriends(int userId, int friendId) {
        Optional<User> optionalUser = userDbStorage.findUserById(userId);
        Optional<User> optionalFriend = userDbStorage.findUserById(friendId);
        if (optionalUser.isPresent()
                && optionalFriend.isPresent()) {
            if (!userDbStorage.checkFriendshipExists(userId, friendId)) {
                userDbStorage.addToFriend(userId, friendId);
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
        Optional<User> optionalUser = userDbStorage.findUserById(userId);
        Optional<User> optionalFriend = userDbStorage.findUserById(friendId);
        if (optionalUser.isPresent()
                && optionalFriend.isPresent()) {
            if (userDbStorage.checkFriendshipExists(userId, friendId)) {
                userDbStorage.removeFromFriends(userId, friendId);
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
        return userDbStorage.showCommonFriends(userId, friendId);
    }

    public Collection<User> showUserFriends(int userId) {
        return userDbStorage.showUserFriendsId(userId);
    }

    public Collection<User> findAllUsers() {
        return userDbStorage.findAllUsers();
    }

    public Optional<User> findUserById(int userId) {
        return userDbStorage.findUserById(userId);
    }

    public User createUser(User user) {
        userValidator.validator(user);
        return userDbStorage.createUser(user);
    }

    public User updateUser(User user) {
        userValidator.validator(user);
        return userDbStorage.updateUser(user);
    }

    public void deleteUserById(int userId) {
        userDbStorage.deleteUserById(userId);
    }
}
