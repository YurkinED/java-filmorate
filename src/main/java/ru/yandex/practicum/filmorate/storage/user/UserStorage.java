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

}
