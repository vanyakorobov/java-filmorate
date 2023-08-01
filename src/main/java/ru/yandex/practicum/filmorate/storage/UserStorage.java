package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserStorage {
    User createUser(User newUser);

    User updateUser(User updatedUser);

    void emailValidation(String email);

    void loginValidation(String login);

    void birthdayValidation(LocalDate birthday);

    boolean nameValidationFailed(String name);

    List<User> getUsersList();

    void chooseLoginOrName(User user, String name, String login);

    int createID();
}
