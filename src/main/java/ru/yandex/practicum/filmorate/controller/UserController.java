package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

    private final FeedService feedService;

    @Autowired
    public UserController(UserService userService, FilmService filmService, FeedService feedService) {
        this.userService = userService;
        this.filmService = filmService;
        this.feedService = feedService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.debug("Получен запрос Get /users. Получить всех пользователей.");
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable @Positive int userId) {
        log.debug("Получен запрос Get /users/{}. Найти пользователя по userId {}.", userId, userId);
        return userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriendsByUserId(@PathVariable int userId) {
        log.debug("Получен запрос Get /users/{}/friends. Найти друзей пользователя по userId {}.", userId, userId);
        userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
        return userService.showUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(@PathVariable @Positive int userId, @PathVariable @Positive int friendId) {
        log.debug("Получен запрос Get /users/{}/friends/common/{}. Найти общих друзей пользователей с userId {} и {}.",
                userId, friendId, userId, friendId);
        return userService.showCommonFriends(userId, friendId);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.debug("Получен запрос Post /users. Создать пользователя {}.", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.debug("Получен запрос Put /users. Обновить данные пользователя {}.", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriendById(@PathVariable int userId, @PathVariable int friendId) {
        log.debug("Получен запрос Put /users/{}/friends/{}. Пользователь {} добавляет в друзья пользователя {}.",
                userId, friendId, userId, friendId);
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriendById(@PathVariable int userId, @PathVariable int friendId) {
        log.debug("Получен запрос Delete /users/{}/friends/{}. Пользователь {} удаляет из друзей пользователя {}.",
                userId, friendId, userId, friendId);
        userService.removeFromFriends(userId, friendId);
    }

    @GetMapping("/{id}/feed")
    public Collection<Feed> getUsersFeeds(@PathVariable int id) {
        log.debug("Получен запрос Get /users/{}/feed. Показать действия пользователя с id {}.",
                id, id);
        userService.findUserById(id).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + id + " нет."));
        return feedService.showUsersFeeds(id);
    }

    @GetMapping("/{userId}/recommendations")
    public List<Film> getRecommendations(@PathVariable int userId) {
        log.info("Получен запрос Get /users/{}/recommendations. Получить рекомендации по фильмама " +
                "для пользователя по userId {}.", userId, userId);
        return filmService.getRecommendations(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        log.debug("Получен запрос Delete /users/{}. Удалить пользователя по userId {}.", userId, userId);
        userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
        userService.deleteUserById(userId);
    }
}