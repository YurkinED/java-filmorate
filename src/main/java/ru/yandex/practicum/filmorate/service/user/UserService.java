package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InvalidIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;


    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addToFriends(int userId, int friendId) {
        if (inMemoryUserStorage.findUserById(userId).isPresent()
                && inMemoryUserStorage.findUserById(friendId).isPresent()) {
            if (inMemoryUserStorage.findUserById(userId).get().addFriendToSet(friendId)
                    && inMemoryUserStorage.findUserById(friendId).get().addFriendToSet(userId)) {
                log.debug("Пользователь {} и {} стали друзьями");
            } else {
                log.debug("Пользователь {} и {} уже друзья");
                throw new InvalidIdException("Пользователи уже являются друзьями, попробуйте другой id.");
            }
        } else {
            log.debug("Неверно введен id {}.");
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public void removeFromFriends(int userId, int friendId) {
        if (inMemoryUserStorage.findUserById(userId).isPresent()
                && inMemoryUserStorage.findUserById(friendId).isPresent()) {
            if (inMemoryUserStorage.findUserById(userId).get().deleteFriendToSet(friendId)
                    && inMemoryUserStorage.findUserById(friendId).get().deleteFriendToSet(userId)) {
                log.debug("Пользователь {} и {} перестали быть друзьями");
            } else {
                log.debug("Пользователь {} и {} не друзья");
                throw new InvalidIdException("Пользователи не являются друзьями, попробуйте другой id.");
            }
        } else {
            log.debug("Неверно введен id {}.");
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public ArrayList<User> showCommonFriends(int userId, int friendId) {
        Optional<User> optionalUser = inMemoryUserStorage.findUserById(userId);
        Optional<User> optionalFriend = inMemoryUserStorage.findUserById(friendId);
        ArrayList<User> commonFriends = new ArrayList<>();

        if (optionalUser.isPresent() && optionalFriend.isPresent()) {
            User user = optionalUser.get();
            User friend = optionalFriend.get();
            ArrayList<Integer> userFriendsSet = user.getFriends();
            ArrayList<Integer> commonFriendsSet = new ArrayList<>();
            commonFriendsSet.addAll(userFriendsSet);
            ArrayList<Integer> friendFriendsSet = friend.getFriends();
            commonFriendsSet.removeIf(element -> !friendFriendsSet.contains(element));
            for (Integer integer : commonFriendsSet) {
                commonFriends.add(inMemoryUserStorage.findUserById(integer).get());
            }
            return commonFriends;
        } else {
            throw new InvalidIdException("Неверно введен id пользователя или друга.");
        }
    }

    public ArrayList<User> showUserFriends(int userId) {
        Optional<User> optionalUser = inMemoryUserStorage.findUserById(userId);

        if (optionalUser.isPresent()) {
            ArrayList<User> friends = new ArrayList<>();
            User user = optionalUser.get();
            ArrayList<Integer> friendsIdSet = user.getFriends();
            for (int id : friendsIdSet) {
                friends.add(inMemoryUserStorage.findUserById(id).get());
            }
            return friends;
        }
        throw new InvalidIdException("Неверно введен id пользователя.");
    }
}