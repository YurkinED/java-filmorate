package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidBirthDateException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class UserValidator {
    public void validator(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new InvalidEmailException("Электронная почта не может быть пустой и должна содержать символ.");
        }
        if (login == null || login.isBlank() || login.contains(" ")) {
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidBirthDateException("Дата рождения не может быть в будущем.");
        }
    }
}
