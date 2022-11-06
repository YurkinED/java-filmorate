package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.constants.SqlQueryConstantsForReview.*;


@Component
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
                .query(SQL_QUERY_GET_ALL_REVIEWS, (rs, rowNum) -> makeReview(rs));
    }

    @Override
    public List<Review> findReviewByFilmId(Integer filmId) {
        return namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_GET_REVIEWS_BY_FILM_ID, (rs, rowNum) -> makeReview(rs), filmId);
    }

    @Override
    public Optional<Review> findReviewById(Integer id) {
        List<Review> reviews = namedParameterJdbcTemplate.getJdbcTemplate()
                .query(SQL_QUERY_GET_REVIEW_BY_ID, (rs, rowNum) -> makeReview(rs), id);
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
        return review;
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
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_ADD_LIKE_OR_DISLIKE, id, userId, isLike);
    }

    @Override
    public void deleteLikeOrDislikeToReview(Integer id, Integer userId) {
        namedParameterJdbcTemplate.getJdbcTemplate().update(SQL_QUERY_DELETE_LIKE_OR_DISLIKE, id, userId);
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

    private Review makeReview(ResultSet rs) throws SQLException {
        int id = rs.getInt("review_id");
        String content = rs.getString("content");
        boolean isPositive = rs.getBoolean("isPositive");
        int useful = rs.getInt("useful");
        int userId = rs.getInt("user_id");
        int filmId = rs.getInt("film_id");
        return new Review(id, content, isPositive, useful, userId, filmId);
    }
}