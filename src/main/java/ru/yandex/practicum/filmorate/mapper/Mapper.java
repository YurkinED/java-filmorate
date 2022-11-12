package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
}
