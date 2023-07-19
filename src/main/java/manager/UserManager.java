package manager;

import exception.ValidationException;
import model.User;

import java.util.*;

public class UserManager {

    private static int userId;
    private Map<Integer, User> users = new HashMap<>();

    public User createUser(User user) throws ValidationException {
        emailUserValidation(user.getEmail());
        loginValidation(user.getLogin());
        nameValidation(user.getName(), user);
        birthdayValidation(user.getBirthday());
        int newUserId = generateUserId();
        users.put(newUserId, user);
        return user;
    }

    public User updateUser(User userUpdate) throws ValidationException {
        emailUserValidation(userUpdate.getEmail());
        loginValidation(userUpdate.getLogin());
        nameValidation(userUpdate.getName(), userUpdate);
        birthdayValidation(userUpdate.getBirthday());
        int id = userUpdate.getId();
        users.put(id, userUpdate);
        return userUpdate;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>(users.values());
        return allUsers;
    }

    public int generateUserId() {
        return ++userId;
    }

    public void emailUserValidation(String email) throws ValidationException {
        if (email != null) {
            if (email.isBlank() || email.contains("@")) {
                throw  new ValidationException("некорректный email");
            }
        }
        throw new ValidationException("email не введён");
    }

    public void loginValidation(String login) throws ValidationException {
        if (login != null) {
            if (login.isBlank() || login.contains(" ")) {
                throw new ValidationException("некорректный login");
            }
        } else {
            throw new ValidationException("поле \"login\" должно быть заполнено!");
        }
    }

    public void nameValidation(String name, User user) {
        if (name == null) {
            name = user.getLogin();
        }
    }

    public void birthdayValidation(Date birthday) throws ValidationException {

        if (birthday.after(new Date())) {
            throw new ValidationException("День рождения не может быть в будущем времени");
        }
    }
}
