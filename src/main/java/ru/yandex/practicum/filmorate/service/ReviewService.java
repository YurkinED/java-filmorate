package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.exceptions.reviewExceptions.LikeOrDislikeReviewException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;

    public Review createReview(Review review) {
        log.debug("Валидация объекта review в методе createReview прошла успешно");
        filmStorage.findFilmById(review.getFilmId()).orElseThrow(() ->
                new InvalidIdException("Фильм с id" + review.getFilmId() + " не найден"));
        userStorage.findUserById(review.getUserId()).orElseThrow(() ->
                new InvalidIdException("Пользователь с id" + review.getUserId() + " не найден"));
        Review reviewForSave = reviewStorage.saveReview(review);
        feedStorage.createFeed(reviewForSave.getUserId(), reviewForSave.getReviewId(),
                Feed.Event.REVIEW, Feed.Operation.ADD);
        log.warn("Добавлена информация в ленту: пользователь id {} добавил отзыв к фильму {}",
                review.getUserId(), review.getFilmId());
        return reviewForSave;
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
        findReviewById(review.getReviewId());
        filmStorage.findFilmById(review.getFilmId()).orElseThrow(() ->
                new InvalidIdException("Фильм с id" + review.getFilmId() + " не найден"));
        userStorage.findUserById(review.getUserId()).orElseThrow(() ->
                new InvalidIdException("Пользователь с id" + review.getUserId() + " не найден"));
        Review updateReview = reviewStorage.updateReview(review);
        feedStorage.createFeed(updateReview.getUserId(), updateReview.getReviewId(), Feed.Event.REVIEW,
                Feed.Operation.UPDATE);
        log.warn("Добавлена информация в ленту: пользователь id {} обновил отзыв к фильму {}",
                updateReview.getUserId(), updateReview.getFilmId());
        return updateReview;
    }

    public void deleteReviewById(Integer id) {
        Review checkReview = findReviewById(id);
        reviewStorage.deleteReviewById(id);
        feedStorage.createFeed(checkReview.getUserId(), checkReview.getFilmId(), Feed.Event.REVIEW,
                Feed.Operation.REMOVE);
        log.warn("Добавлена информация в ленту: пользователь id {} удалил отзыв к фильму {}",
                checkReview.getUserId(), checkReview.getFilmId());
    }

    public void addLikeOrDislikeToReview(Integer id, Integer userId, Boolean isLike) {
        Review review = findReviewById(id);
        userStorage.findUserById(review.getUserId()).orElseThrow(() ->
                new InvalidIdException("Пользователь с id" + userId + " не найден"));
        if (reviewStorage.containsLikeOrDislike(id, userId)) {
            throw new LikeOrDislikeReviewException("Пользователь с id" + userId + " уже поставил оценку этому отзыву");
        }
        reviewStorage.addLikeOrDislikeToReview(id, userId, isLike);
    }

    public void deleteLikeOrDislikeToReview(Integer id, Integer userId) {
        Review review = findReviewById(id);
        if (!reviewStorage.containsLikeOrDislike(id, userId)) {
            throw new LikeOrDislikeReviewException("Оценка отзыва для пользователя с id" + userId + " не найдена");
        }
        reviewStorage.deleteLikeOrDislikeToReview(id, userId);
    }
}
