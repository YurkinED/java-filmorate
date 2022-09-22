package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;

@Slf4j
@Data
public class User {
    private int id;
    @Email(message = "Email должен быть корректным адресом электронной почты.")
    private String email;
    @NotBlank(message = "Необходимо указать login.")
    @Pattern(regexp = "^\\S*$")
    private String login;
    private String name;
    private LocalDate birthday;
    private ArrayList<Integer> friends = new ArrayList<>();

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public boolean addFriendToSet(int friendId) {
        if (friends.contains(friendId)) {
            return false;
        }
        friends.add(friendId);
        log.debug("Пользователь {} добавлен в друзья");
        return true;
    }

    public boolean deleteFriendToSet(int friendId) {
        Integer id = friendId;
        if (friends.contains(id)) {
            friends.remove(id);
            log.debug("Пользователь {} удален из друзей");
            return true;
        }
        return false;
    }
}
