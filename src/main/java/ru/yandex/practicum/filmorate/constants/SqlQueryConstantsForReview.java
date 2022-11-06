package ru.yandex.practicum.filmorate.constants;

public class SqlQueryConstantsForReview {
    public static final String SQL_QUERY_SAVE_REVIEW = "INSERT INTO reviews (content, isPositive, user_id," +
            " film_id) VALUES (:content, :isPositive, :user_id, :film_id)";

    public static final String SQL_QUERY_GET_ALL_REVIEWS = "SELECT * FROM reviews ORDER BY useful DESC";

    public static final String SQL_QUERY_GET_REVIEWS_BY_FILM_ID =
            "SELECT * FROM reviews WHERE film_id = ? ORDER BY useful DESC";

    public static final String SQL_QUERY_GET_REVIEW_BY_ID =
            "SELECT * FROM reviews WHERE review_id = ?";

    public static final String SQL_QUERY_UPDATE_REVIEW =
            "UPDATE reviews SET content = :content, " +
                    "isPositive = :isPositive, useful = :useful, user_id = :user_id, " +
                    "film_id = :film_id WHERE review_id = :review_id";

    public static final String SQL_QUERY_DELETE_REVIEW_BY_ID =
            "DELETE FROM reviews " +
                    "WHERE review_id = ?";

    public static final String SQL_QUERY_GET_LIKES_BY_REVIEW_ID_AND_USER_ID =
            "SELECT * FROM reviews_likes WHERE review_id = ? AND user_id = ?";

    public static final String SQL_QUERY_ADD_LIKE_OR_DISLIKE =
            "INSERT INTO reviews_likes (review_id, user_id, isLike) " +
                    "VALUES (?, ?, ?)";

    public static final String SQL_QUERY_DELETE_LIKE_OR_DISLIKE =
            "DELETE FROM reviews_likes " +
                    "WHERE review_id = ? AND user_id = ?";
}
