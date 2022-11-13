package ru.yandex.practicum.filmorate.constants;

public class SqlQueryConstantsForUser {
    public static final String SQL_QUERY_TAKE_ALL_USERS = "SELECT user_id, email, login, name, birthday FROM users";

    public static final String SQL_QUERY_CREATE_USER = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (:email, :login, :name, :birthday)";
    public static final String SQL_QUERY_UPDATE_USER = "UPDATE users SET email = :email, login = :login, name = :name, " +
            "birthday = :birthday WHERE user_id = :user_id";

    public static final String SQL_QUERY_FIND_USER_BY_ID = "SELECT user_id, email, login, name, birthday FROM users " +
            "WHERE user_id = ?";

    public static final String SQL_QUERY_FIND_FRIENDS_USER_BY_ID = "SELECT user_id, email, login, name, birthday FROM users " +
            "WHERE user_id IN (SELECT second_user_id FROM friends WHERE first_user_id = ?) ";

    public static final String SQL_QUERY_USER_EXISTS = "SELECT user_id, email, login, name, birthday FROM users " +
            "WHERE user_id = ?";
    public static final String SQL_QUERY_TAKE_FRIENDS_BY_USER_ID = "SELECT second_user_id FROM friends " +
            "WHERE first_user_id = ?";
    public static final String SQL_QUERY_ADD_TO_FRIEND = "INSERT INTO friends(first_user_id, second_user_id) " +
            "VALUES (:first_user_id, :second_user_id)";
    public static final String SQL_QUERY_CHECK_FRIENDSHIP_EXISTS = "SELECT first_user_id, second_user_id FROM friends " +
            "WHERE first_user_id = :first_user_id AND second_user_id = :second_user_id";
    public static final String SQL_QUERY_REMOVE_FROM_FRIENDS = "DELETE FROM friends WHERE first_user_id = :first_user_id " +
            "AND second_user_id = :second_user_id";
    public static final String SQL_QUERY_DELETE_USER_BY_ID = "DELETE FROM users WHERE user_id = ?";

    public static final String SQL_QUERY_DELETE_FILM_BY_ID = "DELETE FROM films WHERE film_id = ?";

    public static final String SQL_QUERY_REMOVE_DIRECTOR = "DELETE FROM directors WHERE director_id = :director_id";

    public static final String SQL_QUERY_SHOW_FEEDS_BY_USER_ID = "SELECT f.feed_id, f.user_id, f.entity_id, event_type, operation, creation_time\n" +
            "FROM users u\n" +
            "         JOIN feeds f ON u.user_id = f.USER_ID  WHERE f.user_id = ?";
}
