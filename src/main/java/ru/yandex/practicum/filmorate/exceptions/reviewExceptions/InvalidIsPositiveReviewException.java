package ru.yandex.practicum.filmorate.exceptions.reviewExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidIsPositiveReviewException extends RuntimeException {
    public InvalidIsPositiveReviewException(String message) {
        super(message);
    }
}
