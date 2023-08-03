package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionForResponse;
import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j

public class UserController {
    private InMemoryUserStorage inMemoryUserStorage;
    private UserService userService;

    @Autowired
    public void userService(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getUsersList() {
        List<User> users = inMemoryUserStorage.getUsersList();
        log.info("Выдан список пользователей: " + users);
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserFromTheId(@PathVariable int userId) {
        return inMemoryUserStorage.getUserById(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int otherId, int id) {
        return userService.getCommonFriends(otherId, id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        return userService.getFriendsList(id);
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) throws ValidationException,
            ValidationExceptionForResponse {
        try {
            User createdUser = inMemoryUserStorage.createUser(newUser);
            log.info("добавлен пользователь: " + createdUser);
            return createdUser;

        } catch (ValidationException e) {
            log.info("пользователь не добавлен");
            log.warn(e.getMessage());
            System.out.println(e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException, ValidationExceptionForResponse {
        try {
            User currentUser = inMemoryUserStorage.updateUser(updatedUser);
            log.info("пользователь обновлен: " + currentUser);
            return currentUser;
        } catch (ValidationException e) {
            log.info("пользователь НЕ обновлен");
            log.warn(e.getMessage());
            System.out.println(e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping("/{id}/friends/{friendId}")
    public int addInFriends(@PathVariable int friendId) {
        userService.addFriend(friendId);
        return friendId;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public int deleteInFriends(@PathVariable int friendId) {
        userService.removeFriend(friendId);
        return friendId;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNegativeId(final IllegalArgumentException e) {
        return Map.of("error", "Передан неверный id");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNullId(final NullPointerException e) {
        return Map.of("error", "Не передан id");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("error", "Введены неверные данные");
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RuntimeException ex) {
        return "Страница не найдена: " + ex.getMessage();
    }
}