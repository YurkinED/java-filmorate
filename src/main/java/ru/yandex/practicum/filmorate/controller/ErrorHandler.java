package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.filmExceptions.*;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidBirthDateException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.userExceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleSystemExceptions(final NullPointerException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({InvalidNameException.class, LongDescriptionException.class, NegativeDurationException.class,
            ReleaseDateException.class, InvalidBirthDateException.class, InvalidEmailException.class,
            InvalidLoginException.class, UserAlreadyExistException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateParameterException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({LikesException.class, InvalidIdException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSystemExceptions(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}