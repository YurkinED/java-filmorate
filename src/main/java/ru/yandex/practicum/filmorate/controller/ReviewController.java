package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.validators.ValidationGroup;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    Review createReview(@Valid @RequestBody Review review) {
        log.debug("Получен запрос POST /reviews. Создать отзыв {}", review);
        return reviewService.createReview(review);
    }

    @GetMapping
    List<Review> findAllOrByFilmIdReviews(
            @RequestParam(value = "count", defaultValue = "10", required = false) @Positive Integer count,
            @RequestParam(value = "filmId", required = false) Integer filmId) {
        if (Objects.isNull(filmId)) {
            log.debug("Получен запрос GET /reviews?count={}. Показать {} отзывов", count, count);
            return reviewService.findAllReviewsWithLimit(count);
        } else {
            log.debug("Получен запрос GET /reviews?filmId={}&count={}. Показать {} отзывов для фильма с id{}", filmId, count, count, filmId);
            return reviewService.findReviewByFilmIdWithLimit(filmId, count);
        }
    }

    @GetMapping("/{id}")
    Review findReviewById(@PathVariable Integer id) {
        log.debug("Получен запрос GET /reviews/{}. Показать отзыв с id{}", id, id);
        return reviewService.findReviewById(id);
    }

    @PutMapping
    @Validated({ValidationGroup.OnUpdate.class})
    Review updateReview(@Valid @RequestBody Review review) {
        log.debug("Получен запрос PUT /reviews. Обновить данные отзыва {}", review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    void deleteReview(@PathVariable Integer id) {
        log.debug("Получен запрос DELETE /reviews/{}. Удалить отзыв с id{}", id, id);
        reviewService.deleteReviewById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    void addLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Получен запрос PUT /reviews/{}/like/{}. " +
                "Поставить лайк отзыву с id{} от пользователя с id{}", id, userId, id, userId);
        reviewService.addLikeOrDislikeToReview(id, userId, Boolean.TRUE);
    }

    @PutMapping("/{id}/dislike/{userId}")
    void addDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Получен запрос PUT /reviews/{}/dislike/{}. " +
                "Поставить дизлайк отзыву с id{} от пользователя с id{}", id, userId, id, userId);
        reviewService.addLikeOrDislikeToReview(id, userId, Boolean.FALSE);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void deleteLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Получен запрос DELETE /reviews/{}/like/{}. " +
                "Удалить лайк у отзыва с id{} от пользователя с id{}", id, userId, id, userId);
        reviewService.deleteLikeOrDislikeToReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    void deleteDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("Получен запрос DELETE /reviews/{}/dislike/{}. " +
                "Удалить дизлайк у отзыва с id{} от пользователя с id{}", id, userId, id, userId);
        reviewService.deleteLikeOrDislikeToReview(id, userId);
    }
}
