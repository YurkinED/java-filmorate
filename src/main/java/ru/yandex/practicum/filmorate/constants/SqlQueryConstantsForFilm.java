package ru.yandex.practicum.filmorate.constants;

public class SqlQueryConstantsForFilm {
    public static final String SQL_QUERY_TAKE_FILMS_GENRE_BY_ID = "SELECT g.genre_id, g.genre_name FROM films AS f " +
            "LEFT JOIN films_genres fg on f.film_id = fg.film_id LEFT JOIN genres g on fg.genre_id = g.genre_id " +
            "WHERE f.film_id = ?";

    public static final String SQL_QUERY_CREATE_FILM = "INSERT INTO films (film_name, description, release_date," +
            " duration, mpa_id_in_film) VALUES (:film_name, :description, :release_date, :duration, :mpa_id_in_film)";

    public static final String SQL_QUERY_TAKE_ALL_FILMS_AND_RATINGS = "SELECT f.*, m.mpa_name FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id_in_film = m.mpa_id_in_mpa";

    public static final String SQL_QUERY_INSERT_FILMS_GENRE = "INSERT INTO films_genres (film_id, genre_id) " +
            "VALUES (:film_id, :genre_id)";

    public static final String SQL_QUERY_UPDATE_FILM = "UPDATE films SET film_name = :film_name, " +
            "description = :description, release_date = :release_date, duration = :duration, " +
            "mpa_id_in_film = :mpa_id_in_film WHERE film_id = :film_id";

    public static final String SQL_QUERY_TAKE_ALL_GENRES = "SELECT * FROM genres";
    public static final String SQL_QUERY_TAKE_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";

    public static final String SQL_QUERY_TAKE_ALL_MPA = "SELECT * FROM mpa";

    public static final String SQL_QUERY_TAKE_MPA_BY_ID = "SELECT * FROM mpa WHERE mpa_id_in_mpa = ?";

    public static final String SQL_QUERY_TAKE_FILM_AND_MPA_BY_ID = "SELECT * FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id_in_film = m.mpa_id_in_mpa WHERE film_id = ?";

    public static final String SQL_QUERY_LIKE_FILM = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";

    public static final String SQL_QUERY_CHECK_LIKE = "SELECT * FROM likes WHERE film_id = ? AND user_id = ?";

    public static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM likes " +
            "WHERE film_id = :film_id AND user_id = :user_id";

    public static final String SQL_QUERY_COUNT_LIKES = "SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = ?";

    public static final String SQL_QUERY_DELETE_FILMS_GENRE = "DELETE FROM films_genres WHERE film_id = ?";
    public static final String SQL_QUERY_TAKE_COMMON_FILMS =
            " SELECT " +
            "        f.film_id, " +
            "        f.film_name, " +
            "        f.description, " +
            "        f.description, " +
            "        f.release_date, " +
            "        f.duration, " +
            "        f.mpa_id_in_film, " +
            "        m.mpa_name " +
            " FROM films AS f " +
            " LEFT JOIN mpa AS m ON f.mpa_id_in_film = m.mpa_id_in_mpa "+
            " LEFT JOIN (SELECT film_id, count(*) as cnt " +
            "            FROM likes group by film_id) AS likes_data ON f.film_id=likes_data.film_id "+
            " where f.film_id in       " +
            " (                        " +
            "   SELECT t1.film_id " +
            "   FROM " +
            "       (SELECT film_id " +
            "       FROM likes where user_id=?) AS t1 " +
            "   JOIN (SELECT film_id " +
            "         FROM likes where user_id=?) AS t2 on t1.film_id=t2.film_id " +
            " ) ORDER BY likes_data.cnt DESC NULLS LAST";
}

