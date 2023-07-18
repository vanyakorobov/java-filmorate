package controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import manager.UserManager;
import model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    UserManager userManager = new UserManager();

    @GetMapping("/all-users")
    public List<User> getUsers() {
            List<User> allUsers = userManager.getAllUsers();
            log.info("Выведен список пользователей: " + allUsers);
            return allUsers;
    }

    @PostMapping("/post-user")
    public User postUser(User user) throws ValidationException{
        try {
            userManager.updateUser(user);
            return user;
        } catch (ValidationException exception) {
            log.warn("пользователь не обновлён " + exception.getMessage());
            throw new ValidationException("полььзователь не обновлен");
        }
    }

    @PutMapping("/put-user")
    public User putUser(User user) throws ValidationException {
        try {
            userManager.createUser(user);
            return user;
        } catch (ValidationException exception) {
            log.warn("Пользователь не добавлен " + exception.getMessage());
            throw new ValidationException("Пользователь не добавлен");
        }
    }

    // СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ
    // ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ
    // ПОЛУЧЕНИЕ СПИСКА ВСЕХ ПОЛЬЗОВАТЕЛЕЙ
}
