package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionForResponse;
import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.manager.UsersManager;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UsersManager usersManager = new UsersManager();

    @GetMapping
    public List<User> getUsersList() {
        List<User> users = usersManager.getUsersList();
        log.info("выдан список пользователей" + users);
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) throws ValidationException,
            ValidationExceptionForResponse {
        try {
            User createdUser = usersManager.createUser(newUser);
            log.info("добавлен пользователь" + createdUser);
            return createdUser;

        } catch (ValidationException e) {
            log.info("пользователь не добавлен");
            log.warn(e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException, ValidationExceptionForResponse {
        try {
            User currentUser = usersManager.updateUser(updatedUser);
            log.info("пользователь обновлен " + currentUser);
            return currentUser;
        } catch (ValidationException e) {
            log.info("пользователь не обновлен");
            log.warn(e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }
}