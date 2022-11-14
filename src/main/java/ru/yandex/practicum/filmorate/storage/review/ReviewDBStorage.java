package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.mapper.Mapper;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForReview.*;


@Repository
@RequiredArgsConstructor
public class ReviewDBStorage implements ReviewStorage {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Review saveReview(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = getReviewParameters(review);
        namedParameterJdbcTemplate.update(SQL_QUERY_SAVE_REVIEW, parameters, keyHolder, new String[]{"review_id"});
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        review.setReviewId(id);
        return review;
    }

    @Override
    public List<Review> findAllReviews() {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_GET_ALL_REVIEWS, (rs, rowNum) -> Mapper.makeReview(rs));
    }

    @Override
    public List<Review> findReviewByFilmId(Integer filmId) {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_GET_REVIEWS_BY_FILM_ID, (rs, rowNum) -> Mapper.makeReview(rs), filmId);
    }

    @Override
    public Optional<Review> findReviewById(Integer id) {
        List<Review> reviews = namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_GET_REVIEW_BY_ID, (rs, rowNum) -> Mapper.makeReview(rs), id);
        if (reviews.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(reviews.get(0));
        }
    }

    @Override
    public Review updateReview(Review review) {
        MapSqlParameterSource parameters = getReviewParameters(review);
        namedParameterJdbcTemplate.update(SQL_QUERY_UPDATE_REVIEW, parameters);
        return findReviewById(review.getReviewId()).orElseThrow(
                () -> new InvalidIdException("Отзыв с id" + review.getReviewId() + " не найден")
        );
    }

    @Override
    public void deleteReviewById(Integer id) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_REVIEW_BY_ID, id);
    }

    @Override
    public boolean containsLikeOrDislike(Integer id, Integer userId) {
        return !namedParameterJdbcTemplate.getJdbcTemplate()
                .queryForList(SQL_QUERY_GET_LIKES_BY_REVIEW_ID_AND_USER_ID, id, userId).isEmpty();
    }

    @Override
    public void addLikeOrDislikeToReview(Integer id, Integer userId, Boolean isLike) {
        int useful = isLike ? 1 : -1;
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_ADD_LIKE_OR_DISLIKE, id, userId, useful);
        updateUsefulInReview(id);
    }

    @Override
    public void deleteLikeOrDislikeToReview(Integer id, Integer userId) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_LIKE_OR_DISLIKE, id, userId);
        updateUsefulInReview(id);
    }

    private void updateUsefulInReview(Integer id) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_UPDATE_USEFUL, id);
    }

    private MapSqlParameterSource getReviewParameters(Review review) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("review_id", review.getReviewId());
        parameters.addValue("content", review.getContent());
        parameters.addValue("isPositive", review.getIsPositive());
        parameters.addValue("useful", review.getUseful());
        parameters.addValue("user_id", review.getUserId());
        parameters.addValue("film_id", review.getFilmId());
        return parameters;
    }

}
