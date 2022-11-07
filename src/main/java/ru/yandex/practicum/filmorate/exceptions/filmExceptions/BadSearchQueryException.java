package ru.yandex.practicum.filmorate.exceptions.filmExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadSearchQueryException extends RuntimeException {
    public BadSearchQueryException(String message) {
        super(message);
    }
}
