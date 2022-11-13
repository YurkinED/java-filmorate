package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mapper {
    public static Director makeDirector(ResultSet rs) throws SQLException {
        int id = rs.getInt("director_id");
        String name = rs.getString("director_name");
        return new Director(id, name);
    }

    public static Feed makeFeed(int id, ResultSet rs) throws SQLException {
        return new Feed(rs.getInt("feed_id"),
                id,
                rs.getInt("entity_id"),
                Feed.Event.valueOf(rs.getString("event_type")),
                Feed.Operation.valueOf(rs.getString("operation")),
                rs.getLong("creation_time"));
    }

    public static Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");
        return new Genre(id, name);
    }

    public static Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("mpa_id");
        String name = rs.getString("mpa_name");
        return new Mpa(id, name);
    }

    public static Review makeReview(ResultSet rs) throws SQLException {
        int id = rs.getInt("review_id");
        String content = rs.getString("content");
        boolean isPositive = rs.getBoolean("isPositive");
        int useful = rs.getInt("useful");
        int userId = rs.getInt("user_id");
        int filmId = rs.getInt("film_id");
        return new Review(id, content, isPositive, useful, userId, filmId);
    }

    public static User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    public static User makeUser(SqlRowSet rs) {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    public static Film makeFilm(SqlRowSet filmRows) {
        int id = filmRows.getInt("film_id");
        String name = filmRows.getString("film_name");
        String description = filmRows.getString("description");
        LocalDate releaseDate = Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate();
        long duration = filmRows.getLong("duration");
        int mpaId = filmRows.getInt("mpa_id");
        String mpaName = filmRows.getString("mpa_name");
        Film film = new Film(id, name, description, releaseDate, duration, new Mpa(mpaId, mpaName));
        film.setRating(filmRows.getInt("rating"));
        return film;
    }

    public static Film makeFilmFromRs(ResultSet rs)  {
        try {
            int id = rs.getInt("film_id");
            String name = rs.getString("film_name");
            String description = rs.getString("description");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            long duration = rs.getLong("duration");
            int mpaId = rs.getInt("mpa_id");
            String mpaName = rs.getString("mpa_name");
            Film film = new Film(id, name, description, releaseDate, duration, new Mpa(mpaId, mpaName));
            film.setRating(rs.getInt("rating"));
            for (Director director : makeDirectorFromArray(rs.getString("directors"))) {
                film.addDirectorToFilm(director);
            }
            for (Genre genre : makeGenreFromArray(rs.getString("genres"))) {
                film.addGenresToFilm(genre);
            }

            return film;
        } catch (Exception ex){
            return null;
        }
    }

    public static List<Director> makeDirectorFromArray(String directors) throws SQLException {
        List<Director> directorsList = new ArrayList<>();
        try {
            String rs[]=directors.split(";");
            for(String director:rs){
                directorsList.add(new Director(Integer.parseInt(director.split("#")[0]),director.split("#")[1].strip()));
            }
        } catch (Exception ex){
        }
        return directorsList;
    }

    public static List<Genre> makeGenreFromArray(String genres) throws SQLException {
        List<Genre> genresList=new ArrayList<>();
        try {
            String[] rs = genres.split(";");
            for(String genre:rs){
                genresList.add(new Genre(Integer.parseInt(genre.split("#")[0]), genre.split("#")[1].strip()));
            }
        } catch (Exception ex){

        }
        return genresList;
    }

    public static User makeUserForFriends(SqlRowSet rs) {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

}
