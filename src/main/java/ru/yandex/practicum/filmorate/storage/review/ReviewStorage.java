package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review saveReview(Review review);
    List<Review> findAllReviews();
    List<Review> findReviewByFilmId(Integer filmId);
    Optional<Review> findReviewById(Integer id);
    Review updateReview(Review review);
    void deleteReviewById(Integer id);
    boolean containsLikeOrDislike(Integer id, Integer userId);
    void addLikeOrDislikeToReview(Integer id, Integer userId, Boolean isLike);
    void deleteLikeOrDislikeToReview(Integer id, Integer userId);
}
