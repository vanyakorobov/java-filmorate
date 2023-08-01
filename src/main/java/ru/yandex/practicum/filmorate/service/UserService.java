package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private User user;
    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public void userService(User user, InMemoryUserStorage inMemoryUserStorage) {
        this.user = user;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(int id) {
        user.getFriends().add(id);
        User friend = inMemoryUserStorage.getUserById(id);
        friend.getFriends().add(user.getId());
    }

    public void removeFriend(int id) {
        user.getFriends().remove(id);
        User friend = inMemoryUserStorage.getUserById(id);
        friend.getFriends().remove(user.getId());
    }

    public List<User> getCommonFriends(int friendId) {
        User friend = inMemoryUserStorage.getUserById(friendId);
        List<User> commonFriends = new ArrayList<>();
        for (int id : user.getFriends()) {
            if (friend.getFriends().contains(id)) {
                commonFriends.add(inMemoryUserStorage.getUserById(id));
            }
        }
        return commonFriends;
    }
}
