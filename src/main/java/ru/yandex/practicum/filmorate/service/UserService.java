package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends InMemoryUserStorage {

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
            throw new ObjectNotFoundException("Список друзей пользователя " + userId + " пуст");
        }
        return friends.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();
        Set<Long> commonFriends = new HashSet<>();

        for (Long friendUserId : userFriends) {
            if (friendFriends.contains(friendUserId)) {
                commonFriends.add(friendUserId);
            }
        }

        List<User> commonFriendUsers = new ArrayList<>();
        for (Long commonFriendId : commonFriends) {
            commonFriendUsers.add(getUserById(commonFriendId));
        }

        return commonFriendUsers;
    }
}