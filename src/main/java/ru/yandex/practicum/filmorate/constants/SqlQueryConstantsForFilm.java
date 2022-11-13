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

    public static final String SQL_QUERY_TAKE_ALL_FILMS_AND_RATINGS = "" +
            "SELECT " +
            "f.film_id, " +
            "f.film_name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.mpa_id, " +
            "m.mpa_name, " +
            "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
            "g.genres, " +
            "d.directors " +
            "FROM films AS f " +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            " left join (" +
            "           SELECT fg.film_id," +
            "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
            "           FROM films_genres fg" +
            "           JOIN genres g ON fg.genre_id=g.genre_id" +
            "           group by fg.film_id) g on f.film_id=g.film_id " +
            " left join (" +
            "           SELECT fd.film_id," +
            "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
            "           FROM films_directors fd" +
            "           JOIN directors d ON fd.director_id=d.director_id" +
            "           group by fd.film_id) d on f.film_id=d.film_id " +
            "ORDER BY rating DESC";

    public static final String SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_RATING =
            "SELECT " +
                    "f.film_id, " +
                    "f.film_name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.mpa_name, " +
                    "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
                    "g.genres, " +
                    "d.directors " +
                    "FROM films AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    " LEFT JOIN films_directors fd on f.film_id = fd.film_id " +
                    " left join (" +
                    "           SELECT fg.film_id," +
                    "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
                    "           FROM films_genres fg" +
                    "           JOIN genres g ON fg.genre_id=g.genre_id" +
                    "           group by fg.film_id) g on f.film_id=g.film_id" +
                    " left join (" +
                    "           SELECT fd.film_id," +
                    "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
                    "           FROM films_directors fd" +
                    "           JOIN directors d ON fd.director_id=d.director_id" +
                    "           group by fd.film_id) d on f.film_id=d.film_id " +
                    "WHERE director_id = ? " +
                    "ORDER BY rating DESC";

    public static final String SQL_QUERY_TAKE_DIRECTOR_FILM_AND_SORT_BY_YEAR = "" +
            "SELECT " +
            "f.film_id, " +
            "f.film_name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.mpa_id, " +
            "m.mpa_name, " +
            "fd.director_id, " +
            "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
            "g.genres, " +
            "d.directors " +
            "FROM films AS f " +
            " LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            " left join (" +
            "           SELECT fg.film_id," +
            "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
            "           FROM films_genres fg" +
            "           JOIN genres g ON fg.genre_id=g.genre_id" +
            "           group by fg.film_id) g on f.film_id=g.film_id" +
            " left join (" +
            "           SELECT fd.film_id," +
            "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
            "           FROM films_directors fd" +
            "           JOIN directors d ON fd.director_id=d.director_id" +
            "           group by fd.film_id) d on f.film_id=d.film_id " +
            "LEFT JOIN films_directors fd on f.film_id = fd.film_id WHERE director_id = ? " +
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

    public static final String SQL_QUERY_TAKE_FILM_RATING_AND_MPA_BY_ID = "" +
            "SELECT " +
            "f.film_id, " +
            "f.film_name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.mpa_id, " +
            "m.mpa_name, " +
            "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
            "g.genres, " +
            "d.directors " +
            "FROM films AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
            " left join (" +
            "           SELECT fg.film_id," +
            "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
            "           FROM films_genres fg" +
            "           JOIN genres g ON fg.genre_id=g.genre_id" +
            "           group by fg.film_id) g on f.film_id=g.film_id" +
            " left join (" +
            "           SELECT fd.film_id," +
            "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
            "           FROM films_directors fd" +
            "           JOIN directors d ON fd.director_id=d.director_id" +
            "           group by fd.film_id) d on f.film_id=d.film_id " +
            "WHERE f.film_id = ?";

    public static final String SQL_QUERY_LIKE_FILM = "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)";

    public static final String SQL_QUERY_CHECK_LIKE = "SELECT film_id, user_id FROM likes WHERE film_id = ? " +
            "AND user_id = ?";

    public static final String SQL_QUERY_DELETE_LIKE = "DELETE FROM likes " +
            "WHERE film_id = :film_id AND user_id = :user_id";

    public static final String SQL_QUERY_DELETE_FILMS_GENRE = "DELETE FROM films_genres WHERE film_id = ?";

    public static final String SQL_QUERY_TAKE_RECOMMENDED_FILMS = "SELECT " +
            "f.film_id, " +
            "f.film_name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.mpa_id, " +
            "m.mpa_name, " +
            "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
            "g.genres, " +
            "d.directors " +
            "FROM likes AS l " +
            "JOIN films AS f ON f.film_id = l.film_id " +
            "JOIN mpa AS m ON m.mpa_id =f.mpa_id " +
            " left join (" +
            "           SELECT fg.film_id," +
            "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
            "           FROM films_genres fg" +
            "           JOIN genres g ON fg.genre_id=g.genre_id" +
            "           group by fg.film_id) g on f.film_id=g.film_id" +
            " left join (" +
            "           SELECT fd.film_id," +
            "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
            "           FROM films_directors fd" +
            "           JOIN directors d ON fd.director_id=d.director_id" +
            "           group by fd.film_id) d on f.film_id=d.film_id " +
            "WHERE l.film_id NOT IN(SELECT film_id FROM likes WHERE user_id = ?) " +
            "AND l.user_id IN(SELECT user_id FROM likes " +
            "WHERE film_id IN(SELECT film_id FROM likes WHERE user_id = ?) " +
            "AND user_id !=? " +
            "GROUP BY user_id " +
            "ORDER BY COUNT(film_id))";

    public static final String SQL_QUERY_DELETE_FILMS_DIRECTORS = "DELETE FROM films_directors WHERE film_id = ?";
    public static final String SQL_QUERY_FIND_FILM_FILTER= "" +
            "SELECT  DISTINCT                                   \n" +
            "       f.film_id,                                  \n" +
            "       f.film_name,                                \n" +
            "       f.description,                              \n" +
            "       f.release_date,                             \n" +
            "       f.duration,                                 \n" +
            "       f.mpa_id,                                   \n" +
            "       m.mpa_name,                                 \n" +
            "       (SELECT COUNT(film_id) AS likes FROM likes  WHERE film_id = f.film_id) AS rating,        \n" +
            "        g.genres,                                  \n" +
            "        d.directors                                \n" +
            "FROM films AS f                                    \n" +
            "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id          \n" +
            "LEFT JOIN films_genres fg on f.film_id = fg.film_id\n" +
            " left join (                                       \n" +
            "           SELECT fg.film_id,                      \n" +
            "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres\n" +
            "           FROM films_genres fg                    \n" +
            "           JOIN genres g ON fg.genre_id=g.genre_id \n" +
            "           group by fg.film_id) g on f.film_id=g.film_id\n" +
            " left join (                                       \n" +
            "           SELECT fd.film_id,                      \n" +
            "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors\n" +
            "           FROM films_directors fd                 \n" +
            "           JOIN directors d ON fd.director_id=d.director_id\n" +
            "           group by fd.film_id) d on f.film_id=d.film_id   \n" +
            "JOIN (select CAST(?1 as INTEGER) as genre_id, CAST(?2 as INTEGER) as year_id FROM dual) flt on 1=1  \n" +
            "where (EXTRACT(YEAR FROM f.release_date) = flt.year_id  or flt.year_id='10000') \n" +
            "and  (fg.genre_id = flt.genre_id or flt.genre_id=0)\n" +
            "ORDER BY rating DESC                               \n" +
            "LIMIT ?3                                       \n";

    public static final String SQL_QUERY_TAKE_COMMON_FILMS =
            " SELECT " +
                    "        f.film_id, " +
                    "        f.film_name, " +
                    "        f.description, " +
                    "        f.description, " +
                    "        f.release_date, " +
                    "        f.duration, " +
                    "        f.mpa_id, " +
                    "        m.mpa_name, " +
                    "        g.genres, " +
                    "        d.directors " +
                    " FROM films AS f " +
                    " LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    " LEFT JOIN (SELECT film_id, count(*) as cnt " +
                    "            FROM likes group by film_id) AS likes_data ON f.film_id = likes_data.film_id " +
                    " left join (" +
                    "           SELECT fg.film_id," +
                    "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
                    "           FROM films_genres fg" +
                    "           JOIN genres g ON fg.genre_id=g.genre_id" +
                    "           group by fg.film_id) g on f.film_id=g.film_id" +
                    " left join (" +
                    "           SELECT fd.film_id," +
                    "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
                    "           FROM films_directors fd" +
                    "           JOIN directors d ON fd.director_id=d.director_id" +
                    "           group by fd.film_id) d on f.film_id=d.film_id " +
                    " where f.film_id in       " +
                    " (                        " +
                    "   SELECT t1.film_id " +
                    "   FROM " +
                    "       (SELECT film_id " +
                    "       FROM likes where user_id=?) AS t1 " +
                    "   JOIN (SELECT film_id " +
                    "         FROM likes where user_id=?) AS t2 on t1.film_id=t2.film_id " +
                    " ) ORDER BY likes_data.cnt DESC NULLS LAST";
    public static final String SQL_QUERY_SEARCH_FILMS_BY_TITLE =
            "SELECT f.film_id, " +
                    "f.film_name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.mpa_name, " +
                    "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
                    "g.genres, " +
                    "d.directors " +
                    "FROM films AS f " +
                    "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    " left join (" +
                    "           SELECT fg.film_id," +
                    "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
                    "           FROM films_genres fg" +
                    "           JOIN genres g ON fg.genre_id=g.genre_id" +
                    "           group by fg.film_id) g on f.film_id=g.film_id" +
                    " left join (" +
                    "           SELECT fd.film_id," +
                    "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
                    "           FROM films_directors fd" +
                    "           JOIN directors d ON fd.director_id=d.director_id" +
                    "           group by fd.film_id) d on f.film_id=d.film_id " +
                    "WHERE LOWER(f.film_name) LIKE ? ORDER BY rating DESC";

    public static final String SQL_QUERY_SEARCH_FILMS_BY_DIRECTOR =
            "SELECT f.film_id, " +
                    "f.film_name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.mpa_name, " +
                    "(SELECT COUNT(film_id) AS likes FROM likes WHERE film_id = f.film_id) AS rating, " +
                    "g.genres, " +
                    "d.directors " +
                    "FROM films AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "INNER JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                    "INNER JOIN directors AS d ON fd.director_id = d.director_id " +
                    " left join (" +
                    "           SELECT fg.film_id," +
                    "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
                    "           FROM films_genres fg" +
                    "           JOIN genres g ON fg.genre_id=g.genre_id" +
                    "           group by fg.film_id) g on f.film_id=g.film_id" +
                    " left join (" +
                    "           SELECT fd.film_id," +
                    "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
                    "           FROM films_directors fd" +
                    "           JOIN directors d ON fd.director_id=d.director_id" +
                    "           group by fd.film_id) d on f.film_id=d.film_id " +
                    "WHERE LOWER(d.director_name) LIKE ? ORDER BY rating DESC";

    public static final String SQL_QUERY_SEARCH_FILMS_BY_TITLE_AND_DIRECTOR =
            "SELECT f.film_id, f.film_name, f.description, " +
                    "f.release_date, f.duration, f.mpa_id, m.mpa_name, (SELECT COUNT(film_id) AS likes FROM likes " +
                    "WHERE film_id = f.film_id) AS rating, " +
                    "g.genres, " +
                    "d.directors " +
                    "FROM films AS f LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                    "LEFT JOIN films_directors AS fd ON f.film_id = fd.film_id " +
                    "LEFT JOIN directors AS d ON fd.director_id = d.director_id " +
                    " left join (" +
                    "           SELECT fg.film_id," +
                    "                  LISTAGG(g.genre_id||'#'||g.genre_name,';')  as genres" +
                    "           FROM films_genres fg" +
                    "           JOIN genres g ON fg.genre_id=g.genre_id" +
                    "           group by fg.film_id) g on f.film_id=g.film_id" +
                    " left join (" +
                    "           SELECT fd.film_id," +
                    "                 LISTAGG(d.director_id||'#'||d.director_name,';')  as directors" +
                    "           FROM films_directors fd" +
                    "           JOIN directors d ON fd.director_id=d.director_id" +
                    "           group by fd.film_id) d on f.film_id=d.film_id " +
                    "WHERE LOWER(d.director_name) LIKE ? OR LOWER(f.film_name) LIKE ? ORDER BY rating DESC";
}

