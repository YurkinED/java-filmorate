package ru.yandex.practicum.filmorate.exceptions.reviewExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidFilmReviewException extends RuntimeException {
    public InvalidFilmReviewException(String message) {
        super(message);
    }
}
