package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    Review createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @GetMapping
    List<Review> findAllOrByFilmIdReviews(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
            @RequestParam(value = "filmId", required = false) Integer filmId) {
        if (Objects.isNull(filmId)) {
            return reviewService.findAllReviewsWithLimit(count);
        } else {
            return reviewService.findReviewByFilmIdWithLimit(filmId, count);
        }
    }

    @GetMapping("/{id}")
    Review findReviewById(@PathVariable Integer id) {
        return reviewService.findReviewById(id);
    }

    @PutMapping
    Review updateReview(@RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReviewById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    void addLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addLikeOrDislikeToReview(id, userId, Boolean.TRUE);
    }

    @PutMapping("/{id}/dislike/{userId}")
    void addDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.addLikeOrDislikeToReview(id, userId, Boolean.FALSE);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void deleteLikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteLikeOrDislikeToReview(id, userId, Boolean.TRUE);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    void deleteDislikeToReview(@PathVariable Integer id, @PathVariable Integer userId) {
        reviewService.deleteLikeOrDislikeToReview(id, userId, Boolean.FALSE);
    }
}
