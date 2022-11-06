package ru.yandex.practicum.filmorate.exceptions.reviewExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserReviewException extends RuntimeException {
    public InvalidUserReviewException(String message) {
        super(message);
    }
}
