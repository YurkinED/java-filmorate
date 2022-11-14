package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.*;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.*;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidBirthDateException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final IllegalArgumentException e) {
        log.debug("Упс. Кажется, возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final NullPointerException e) {
        log.debug("Упс. Кажется, возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final Throwable e) {
        log.debug("Упс. Кажется, возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity< String > exc(ConstraintViolationException ex){
        log.debug("Упс. Кажется, возникла ошибка {},", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidNameException.class, LongDescriptionException.class, NegativeDurationException.class,
            ReleaseDateException.class, InvalidBirthDateException.class, InvalidEmailException.class,
            InvalidLoginException.class, UserAlreadyExistException.class,
            LikeOrDislikeReviewException.class, BadSearchQueryException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateParameterException(final RuntimeException e) {
        log.debug("Упс. Кажется, возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({LikesException.class, InvalidIdException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSystemExceptions(final RuntimeException e) {
        log.debug("Упс. Кажется, возникла ошибка {},", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
