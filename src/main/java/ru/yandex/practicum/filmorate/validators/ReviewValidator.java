package ru.yandex.practicum.filmorate.validators;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.EmptyContentReviewException;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.InvalidFilmReviewException;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.InvalidIsPositiveReviewException;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.InvalidUserReviewException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Objects;

@Component
public class ReviewValidator {

    public void validate(Review review) {
        if (Objects.isNull(review.getContent())) {
            throw new EmptyContentReviewException("Поле content не может быть null");
        }
        if (Objects.isNull(review.getFilmId())) {
            throw new InvalidFilmReviewException("Id фильма не может быть null");
        }
        if (Objects.isNull(review.getUserId())) {
            throw new InvalidUserReviewException("Id пользователя не может быть null");
        }
        if (Objects.isNull(review.getIsPositive())) {
            throw new InvalidIsPositiveReviewException("Поле isPositive не может быть null");
        }
    }
}
