package ru.yandex.practicum.filmorate.constants;

public class SqlQueryConstantsForFilm {
    public static final String SQL_QUERY_TAKE_FILMS_GENRE_AND_DIRECTOR_BY_ID = "SELECT g.genre_id, g.genre_name, " +
            "d.director_id, d.director_name FROM films AS f LEFT JOIN films_genres fg on f.film_id = fg.film_id " +
            "LEFT JOIN genres g on fg.genre_id = g.genre_id LEFT JOIN films_directors fd on f.film_id = fd.film_id " +
            "LEFT JOIN directors d on d.director_id = fd.director_id WHERE f.film_id = ?";

    public static final String SQL_QUERY_CREATE_FILM = "INSERT INTO films (film_name, description, release_date," +
            " duration, mpa_id) VALUES (:film_name, :description, :release_date, :duration, :mpa_id)";
    public static final String SQL_QUERY_CREATE_DIRECTOR = "INSERT INTO directors (director_name) " +
            "VALUES (:director_name)";

    public static final String SQL_QUERY_TAKE_ALL_FILMS_AND_RATINGS = "SELECT f.film_id, f.film_name, f.description, " +
            "f.release_date, f.duration, f.mpa_id, m.mpa_name, (SELECT COUNT(film_id) AS likes FROM likes " +
            "WHERE film_id = f.film_id) AS rating FROM films AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            "ORDER BY rating DESC";
    public static final String SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING = "SELECT f.film_id, f.film_name, " +
            "f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name, fd.director_id, (SELECT COUNT(film_id) " +
            "AS likes FROM likes WHERE film_id = f.film_id) AS rating FROM films AS f LEFT JOIN mpa AS m ON " +
            "f.mpa_id = m.mpa_id LEFT JOIN films_directors fd on f.film_id = fd.film_id WHERE director_id = ? " +
            "ORDER BY rating DESC";

    public static final String SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR = "SELECT f.film_id, f.film_name, " +
            "f.description, f.release_date, f.duration, f.mpa_id, m.mpa_name, fd.director_id, (SELECT COUNT(film_id) " +
            "AS likes FROM likes WHERE film_id = f.film_id) AS rating FROM films AS f LEFT JOIN mpa AS m ON " +
            "f.mpa_id = m.mpa_id LEFT JOIN films_directors fd on f.film_id = fd.film_id WHERE director_id = ? " +
            "ORDER BY release_date";

    public static final String SQL_QUERY_INSERT_FILMS_GENRE = "INSERT INTO films_genres (film_id, genre_id) " +
            "VALUES (:film_id, :genre_id)";

    public static final String SQL_QUERY_INSERT_FILMS_DIRECTOR = "INSERT INTO films_directors (film_id, director_id) " +
            "VALUES (:film_id, :director_id)";

    public static final String SQL_QUERY_UPDATE_FILM = "UPDATE films SET film_name = :film_name, " +
            "description = :description, release_date = :release_date, duration = :duration, " +
            "mpa_id = :mpa_id WHERE film_id = :film_id";

    public static final String SQL_QUERY_UPDATE_DIRECTOR = "UPDATE directors SET director_name = :director_name";

    public static final String SQL_QUERY_TAKE_ALL_GENRES = "SELECT genre_id, genre_name FROM genres";
    public static final String SQL_QUERY_TAKE_ALL_DIRECTORS = "SELECT director_id, director_name FROM directors";
    public static final String SQL_QUERY_TAKE_GENRE_BY_ID = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
    public static final String SQL_QUERY_TAKE_DIRECTOR_BY_ID = "SELECT director_id, director_name FROM directors " +
            "WHERE director_id = ?";

    public static final String SQL_QUERY_TAKE_ALL_MPA = "SELECT mpa_id, mpa_name FROM mpa";

    public static final String SQL_QUERY_TAKE_MPA_BY_ID = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";

    public static final String SQL_QUERY_TAKE_FILM_RATING_AND_MPA_BY_ID = "SELECT f.film_id, f.film_name, f.description, " +
            "f.release_date, f.duration, f.mpa_id, m.mpa_name, (SELECT COUNT(film_id) AS likes FROM likes " +
            "WHERE film_id = f.film_id) AS rating FROM films AS f LEFT JOIN mpa AS m ON " +
            "f.mpa_id= m.mpa_id WHERE film_id = ?";

    public static final String SQL_QUERY_LIKE_FILM = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";

    public static final String SQL_QUERY_CHECK_LIKE = "SELECT film_id, user_id FROM likes WHERE film_id = ? " +
            "AND user_id = ?";

    public static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM likes " +
            "WHERE film_id = :film_id AND user_id = :user_id";

    public static final String SQL_QUERY_DELETE_FILMS_GENRE = "DELETE FROM films_genres WHERE film_id = ?";
    public static final String SQL_QUERY_DELETE_FILMS_DIRECTORS = "DELETE FROM films_directors WHERE film_id = ?";
}
