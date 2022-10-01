package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;


    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addToFriends(int userId, int friendId) {
        Optional<User> optionalUser = inMemoryUserStorage.findUserById(userId);
        Optional<User> optionalFriend = inMemoryUserStorage.findUserById(friendId);
        if (optionalUser.isPresent()
                && optionalFriend.isPresent()) {
            if (optionalUser.get().addFriendToSet(friendId)
                    && optionalFriend.get().addFriendToSet(userId)) {
                log.warn("Пользователь {} и {} стали друзьями");
            } else {
                log.warn("Пользователь {} и {} уже друзья");
                throw new InvalidIdException("Пользователи уже являются друзьями, попробуйте другой id.");
            }
        } else {
            log.warn("Неверно введен id {}.");
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public void removeFromFriends(int userId, int friendId) {
        Optional<User> optionalUser = inMemoryUserStorage.findUserById(userId);
        Optional<User> optionalFriend = inMemoryUserStorage.findUserById(friendId);
        if (optionalUser.isPresent()
                && optionalFriend.isPresent()) {
            if (optionalUser.get().deleteFriendToSet(friendId)
                    && optionalFriend .get().deleteFriendToSet(userId)) {
                log.warn("Пользователь {} и {} перестали быть друзьями");
            } else {
                log.warn("Пользователь {} и {} не друзья");
                throw new InvalidIdException("Пользователи не являются друзьями, попробуйте другой id.");
            }
        } else {
            log.warn("Неверно введен id {}.");
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public List<User> showCommonFriends(int userId, int friendId) {
            return inMemoryUserStorage.findUserById(userId)
                    .orElseThrow(() -> new InvalidIdException("Нет пользователя с таким id")).getFriends().stream()
                    .filter(id -> inMemoryUserStorage.findUserById(friendId)
                            .orElseThrow(() -> new InvalidIdException("Нет друга с таким id"))
                            .getFriends().contains(id))
                    .map(commonId -> inMemoryUserStorage.findUserById(commonId).orElse(null))
                    .collect(Collectors.toList());
        }

    public List<User> showUserFriends(int userId) {
        return inMemoryUserStorage.findUserById(userId)
                .orElseThrow(() -> new InvalidIdException("Нет пользователя с таким id")).getFriends().stream()
                .map(commonId -> inMemoryUserStorage.findUserById(commonId).orElse(null))
                .collect(Collectors.toList());
    }

    public Collection<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    public Optional<User> findUserById(int userId) {
        return inMemoryUserStorage.findUserById(userId);
    }

    public User createUser(User user) {
        return inMemoryUserStorage.createUser(user);
    }

    public User updateUser(@Valid User user) {
        return inMemoryUserStorage.updateUser(user);
    }
}
