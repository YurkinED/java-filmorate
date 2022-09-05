package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.*;
import ru.yandex.practicum.filmorate.helpTools.IdCounter;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    UserValidator userValidator = new UserValidator();
    private final IdCounter idCounter = new IdCounter();

    @GetMapping
    public Collection<User> findAll() {
        log.debug("Получен запрос GET /users.");
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        user.setId(idCounter.incrementIdCounter());
        userValidator.validator(user);
        int id = user.getId();
        String name = user.getName();
        if (users.containsKey(id)) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    id + " уже зарегистрирован.");
        }
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        userValidator.validator(user);
        String name = user.getName();
        int id = user.getId();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(id)) {
            users.put(id, user);
            return user;
        } else {
            throw new InvalidIdException("Не удалось обновить пользователя. Нет пользователя с id: " + id);
        }
    }
}