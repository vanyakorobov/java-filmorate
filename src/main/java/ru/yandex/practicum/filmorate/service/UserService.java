package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public void userService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(int id, int friendId) {
        User i = inMemoryUserStorage.getUserById(id);
        User friend = inMemoryUserStorage.getUserById(friendId);
        Set<Integer> myFriends = i.getFriends();
        myFriends = new HashSet<>();
        myFriends.add(i.getId());
        Set<Integer> friendsOfFriends = friend.getFriends();
        friendsOfFriends = new HashSet<>();
        friendsOfFriends.add(friend.getId());
    }

    public List<User> getFriendsList(int id) {
        User user = inMemoryUserStorage.getUserById(id); // получаем Юзера, чьи друзья нужны
        List<User> friends = new ArrayList<>();  // Создали новый список
        for (Integer friendId : user.getFriends()) {
            User friend = inMemoryUserStorage.getUserById(friendId); // Получаем друга по id
            friends.add(friend); // Добавляем друга в список
        }
        return friends;
    }

    public void removeFriend(int id) {
        User i = inMemoryUserStorage.getUserById(id);
        i.getFriends().remove(id);
        User friend = inMemoryUserStorage.getUserById(id);
        friend.getFriends().remove(i.getId());
    }

    public List<User> getCommonFriends(int friendId, int myId) {
        User friend = inMemoryUserStorage.getUserById(friendId);
        User i = inMemoryUserStorage.getUserById(myId);
        List<User> commonFriends = new ArrayList<>();
        for (int id : i.getFriends()) {
            if (friend.getFriends().contains(id)) {
                commonFriends.add(inMemoryUserStorage.getUserById(id));
            }
        }
        return commonFriends;
    }
}
