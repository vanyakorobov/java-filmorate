package controller;

import exception.ValidationException;
import exception.ValidationExceptionForResponse;
import lombok.extern.slf4j.Slf4j;
import manager.Managers;
import manager.UsersManager;
import model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UsersManager usersManager = Managers.getDefaultUsersManager();

    @GetMapping
    public List<User> getUsersList() {
        List<User> users = usersManager.getUsersList();
        log.info("🟩 список пользователей выдан: " + users);
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) throws ValidationException,
            ValidationExceptionForResponse {
        try {
            User createdUser = usersManager.createUser(newUser);
            log.info("🟩 добавлен пользователь: " + createdUser);
            return createdUser;

        } catch (ValidationException e) {
            log.info("🟩 пользователь НЕ добавлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException, ValidationExceptionForResponse {
        try {
            User currentUser = usersManager.updateUser(updatedUser);
            log.info("🟩 пользователь обновлен: " + currentUser);
            return currentUser;
        } catch (ValidationException e) {
            log.info("🟩 пользователь НЕ обновлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }
}