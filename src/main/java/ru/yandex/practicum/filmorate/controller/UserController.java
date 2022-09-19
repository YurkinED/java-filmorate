package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return inMemoryUserStorage.findAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        return inMemoryUserStorage.findUserById(userId).orElseThrow(
                () -> new InvalidIdException("К сожалению, пользователя с id " + userId + " нет."));
    }

    @GetMapping("/{userId}/friends")
    public ArrayList<User> getFriendsByUserId(@PathVariable int userId) {
        return userService.showUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public ArrayList<User> getCommonFriends(@PathVariable int userId, @PathVariable int friendId) {
        return userService.showCommonFriends(userId, friendId);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return inMemoryUserStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriendById(@PathVariable int userId, @PathVariable int friendId) {
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriendById(@PathVariable int userId, @PathVariable int friendId) {
        userService.removeFromFriends(userId, friendId);
    }

}