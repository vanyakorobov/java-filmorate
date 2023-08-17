package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    int currentID;

    private Map<Integer, User> users = new HashMap();

    public User createUser(User newUser) throws ValidationException {
        emailValidation(newUser.getEmail());
        birthdayValidation(newUser.getBirthday());
        String userLogin = newUser.getLogin();
        loginValidation(userLogin);
        chooseLoginOrName(newUser, newUser.getName(), userLogin);
        newUser.setId(createID());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User updateUser(User updatedUser) throws ValidationException {
        if (users.containsKey(updatedUser.getId())) {
            users.put(updatedUser.getId(), updatedUser);
        }
        return updatedUser;
    }

    public void emailValidation(String email) {
        if (email != null) {
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    throw new ValidationException("пользователь с таким email уже существует");
                }
            }
            if (email.isBlank() || !email.contains("@")) {
                throw new ValidationException("некорректный email! ваш email: " + email);
            }
        } else {
            throw new ValidationException("поле \"mail\" должно быть заполнено. Ваш email: " + email);
        }
    }

    public void loginValidation(String login) {
        if (login != null) {
            if (login.isBlank() || login.contains(" ")) {
                throw new ValidationException("некорректный login");
            }
        } else {
            throw new ValidationException("поле \"login\" должно быть заполнено");
        }
    }

    public void birthdayValidation(LocalDate birthday) {
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                throw new ValidationException("birthday не может быть в будущем");
            }
        } else {
            throw new ValidationException("поле \"birthday\" должно быть заполнено");
        }
    }

    public boolean nameValidationFailed(String name) {
        if (name == null || name.isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    public List<User> getUsersList() {
        List<User> list = new ArrayList<>(users.values());
        return list;
    }

    public User getUserById(int id) {
        User user = users.get(id);
        return user;
    }

    public void chooseLoginOrName(User user, String name, String login) {
        if (nameValidationFailed(name)) {
            user.setName(login);
        } else {
            user.setName(name);
        }
    }

    public int createID() {
        return ++currentID;
    }
}