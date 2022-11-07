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
        log.debug("Валидация объекта review в методе createReview прошла успешно");
        filmStorage.findFilmById(review.getFilmId()).orElseThrow(()->
                new InvalidIdException("Фильм с id" + review.getFilmId() + " не найден"));
        userStorage.findUserById(review.getUserId()).orElseThrow(()->
                new InvalidIdException("Пользователь с id" + review.getUserId() + " не найден"));
        userStorage.createFeed (review.getUserId(), review.getFilmId(), 2,2);
        log.warn("Добавлена информация в ленту: пользователь id {} добавил отзыв к фильму {}",
                review.getUserId(), review.getFilmId());
        return reviewStorage.saveReview(review);
    }

    public List<Review> findAllReviewsWithLimit(Integer count) {
        return reviewStorage.findAllReviews()
                .stream()
                .limit(count)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Review> findReviewByFilmIdWithLimit(Integer filmId, Integer count) {
        return reviewStorage.findReviewByFilmId(filmId)
                .stream()
                .limit(count)
                .collect(Collectors.toUnmodifiableList());
    }

    public Review findReviewById(Integer id) {
        return reviewStorage.findReviewById(id).orElseThrow(
                () -> new InvalidIdException("Отзыв с id" + id + " не найден")
        );
    }

    public Review updateReview(Review review) {
        Review checkReview = findReviewById(review.getReviewId());
        /*if (review.getUserId() != checkReview.getUserId()){
            throw new RuntimeException();
        }*/
        Review updateReviewCreate = review;
        updateReviewCreate.setUserId(checkReview.getUserId());
        updateReviewCreate.setFilmId(checkReview.getFilmId());
        updateReviewCreate.setUseful(checkReview.getUseful());
        reviewValidator.validate(review);
        filmStorage.findFilmById(review.getFilmId()).orElseThrow(()->
                new InvalidIdException("Фильм с id" + review.getFilmId() + " не найден"));
        userStorage.findUserById(review.getUserId()).orElseThrow(()->
                new InvalidIdException("Пользователь с id" + review.getUserId() + " не найден"));
        userStorage.createFeed (checkReview.getUserId(), checkReview.getFilmId(), 2,3);
        log.warn("Добавлена информация в ленту: пользователь id {} обновил отзыв к фильму {}",
                checkReview.getUserId(),checkReview.getFilmId());
            Review updateReview = reviewStorage.updateReview(updateReviewCreate);
            return updateReview;
    }

    public void deleteReviewById(Integer id) {
        Review checkReview = findReviewById(id);
        reviewStorage.deleteReviewById(id);
        userStorage.createFeed (checkReview.getUserId(),checkReview.getFilmId(), 2,1);
        log.warn("Добавлена информация в ленту: пользователь id {} удалил отзыв к фильму {}",
                checkReview.getUserId(), checkReview.getFilmId());
    }

    public void addLikeOrDislikeToReview(Integer id, Integer userId, Boolean isLike) {
        Review review = findReviewById(id);
        userStorage.findUserById(review.getUserId()).orElseThrow(()->
                new InvalidIdException("Пользователь с id" + userId + " не найден"));
        if (reviewStorage.containsLikeOrDislike(id, userId)) {
            throw new LikeOrDislikeReviewException("Пользователь с id" + userId + " уже поставил оценку этому отзыву");
        }
        reviewStorage.addLikeOrDislikeToReview(id, userId, isLike);
        if (isLike) {
            review.setUseful(review.getUseful() + 1);
        } else {
            review.setUseful(review.getUseful() - 1);
        }
        reviewStorage.updateReview(review);
    }

    public void deleteLikeOrDislikeToReview(Integer id, Integer userId, Boolean isLike) {
        Review review = findReviewById(id);
        if (!reviewStorage.containsLikeOrDislike(id, userId)) {
            throw new LikeOrDislikeReviewException("Оценка отзыва для пользователя с id" + userId + " не найдена");
        }
        reviewStorage.deleteLikeOrDislikeToReview(id, userId);
        if (isLike) {
            review.setUseful(review.getUseful() - 1);
        } else {
            review.setUseful(review.getUseful() + 1);
        }
        reviewStorage.updateReview(review);
    }
}
