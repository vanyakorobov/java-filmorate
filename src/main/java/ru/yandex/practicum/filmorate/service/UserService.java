package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends InMemoryUserStorage {
    private final UserStorage userStorage;

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        Set<Long> friends = user.getFriends();
        if (friends.isEmpty()) {
            throw new ObjectNotFoundException("писок друзей юзера" + userId + "пуст");
        }
        return friends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();
        if (userFriends.stream().anyMatch(friendFriends::contains)) {
            return userFriends.stream()
                    .filter(userFriends::contains)
                    .filter(friendFriends::contains)
                    .map(userStorage::getUserById).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}