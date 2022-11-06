package ru.yandex.practicum.filmorate.exceptions.reviewExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmptyContentReviewException extends RuntimeException {
    public EmptyContentReviewException(String message) {
        super(message);
    }
}
