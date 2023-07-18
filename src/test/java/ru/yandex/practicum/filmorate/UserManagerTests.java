package ru.yandex.practicum.filmorate;

import exception.ValidationException;
import manager.UserManager;
import model.User;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserManagerTests {
UserManager userManager = new UserManager();
    Map<Integer, User> users;

    @Test
    void createUser() throws ValidationException {
        Date birthday = new Date(2021, 6, 7);
        User user = new User.UserBuilder()
                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        User createdUser = userManager.createUser(user);

        assertEquals(1, createdUser.getId(), "ID созданного пользователя != 1");
        assertEquals("some@yandex.ru", createdUser.getEmail());
        assertEquals(1, users.size(), "размер мапы != 1");
    }

    @Test
    void createUserWithIncorrectName() throws ValidationException {
        Date date = new Date(2021, 6, 7);
        User user = new User.UserBuilder()
                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .birthday(date)
                .build();
        User createdUser = userManager.createUser(user);

        assertEquals(1, createdUser.getId(), "ID созданного пользователя != 1");
        assertEquals("userLogin", createdUser.getName());
        assertEquals(1, users.size(), "размер мапы != 1");
    }
    @Test
    void doNotCreateUserWithIncorrectBirthday() {
        Date date = new Date(3021, 6, 7);
        User user = new User.UserBuilder()

                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(date)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userManager.createUser(user)
        );

        assertEquals("🔹birthday не может быть в будущем", exception.getMessage());
        assertEquals(0, users.size(), "размер мапы != 0");
    }
}
