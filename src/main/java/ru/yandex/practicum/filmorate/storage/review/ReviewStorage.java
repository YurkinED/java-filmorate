package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review save(Review review);
    List<Review> findAll();
    List<Review> findByFilmId(Integer filmId);
    Optional<Review> findById(Integer id);
    Review update(Review review);
    void deleteById(Integer id);
    boolean containsLikeOrDislike(Integer id, Integer userId);
    void addLikeOrDislike(Integer id, Integer userId, Boolean isLike);
    void deleteLikeOrDislike(Integer id, Integer userId);
}
