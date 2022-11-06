package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.debug("Получен запрос Get /users. Получить всех пользователей.");
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable int userId) {
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
    public Collection<User> getCommonFriends(@PathVariable int userId, @PathVariable int friendId) {
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

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable int userId) {
        log.debug("Получен запрос Delete /users/{}. Удалить пользователя по userId {}.", userId, userId);
        userService.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
        userService.deleteUserById(userId);
    }

}