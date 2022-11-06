package ru.yandex.practicum.filmorate.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.LikeOrDislikeReviewException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.validators.ReviewValidator;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewValidator reviewValidator;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    public Review createReview(Review review) {
        reviewValidator.validate(review);
        filmStorage.findFilmById(review.getFilmId()).orElseThrow(()->
                new InvalidIdException("Фильм с id" + review.getFilmId() + " не найден"));
        userStorage.findUserById(review.getUserId()).orElseThrow(()->
                new InvalidIdException("Пользователь с id" + review.getUserId() + " не найден"));
        return reviewStorage.save(review);
    }

    public List<Review> findAllWithLimitCount(Integer count) {
        return reviewStorage.findAll()
                .stream()
                .limit(count)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Review> findByFilmIdWithLimitCount(Integer filmId, Integer count) {
        return reviewStorage.findByFilmId(filmId)
                .stream()
                .limit(count)
                .collect(Collectors.toUnmodifiableList());
    }

    public Review findById(Integer id) {
        return reviewStorage.findById(id).orElseThrow(
                () -> new InvalidIdException("Отзыв с id" + id + " не найден")
        );
    }

    public Review updateReview(Review review) {
        findById(review.getReviewId());
        reviewValidator.validate(review);
        filmStorage.findFilmById(review.getFilmId()).orElseThrow(()->
                new InvalidIdException("Фильм с id" + review.getFilmId() + " не найден"));
        userStorage.findUserById(review.getUserId()).orElseThrow(()->
                new InvalidIdException("Пользователь с id" + review.getUserId() + " не найден"));
        return reviewStorage.update(review);
    }

    public void deleteReview(Integer id) {
        findById(id);
        reviewStorage.deleteById(id);
    }

    public void addLikeOrDislike(Integer id, Integer userId, Boolean isLike) {
        Review review = findById(id);
        userStorage.findUserById(review.getUserId()).orElseThrow(()->
                new InvalidIdException("Пользователь с id" + userId + " не найден"));
        if (reviewStorage.containsLikeOrDislike(id, userId)) {
            throw new LikeOrDislikeReviewException("Пользователь с id" + userId + " уже поставил оценку этому отзыву");
        }
        reviewStorage.addLikeOrDislike(id, userId, isLike);
        if (isLike) {
            review.setUseful(review.getUseful() + 1);
        } else {
            review.setUseful(review.getUseful() - 1);
        }
        reviewStorage.update(review);
    }

    public void deleteLikeOrDislike(Integer id, Integer userId, Boolean isLike) {
        Review review = findById(id);
        if (!reviewStorage.containsLikeOrDislike(id, userId)) {
            throw new LikeOrDislikeReviewException("Оценка отзыва для пользователя с id" + userId + " не найдена");
        }
        reviewStorage.deleteLikeOrDislike(id, userId);
        if (isLike) {
            review.setUseful(review.getUseful() - 1);
        } else {
            review.setUseful(review.getUseful() + 1);
        }
        reviewStorage.update(review);
    }
}
