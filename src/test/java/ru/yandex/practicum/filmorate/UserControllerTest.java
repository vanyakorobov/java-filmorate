package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class UserControllerTest {
    private InMemoryUserStorage storage = new InMemoryUserStorage();
    private UserService service = new UserService(storage);
    private UserController controller = new UserController(storage, service);
    private final User user = new User(1L, "vanya@yandex.ru", "login", "vanya",
            LocalDate.of(1985, 1, 1), new HashSet<>());
    private final User updatedUser = new User(1L, "vanya1@yandex.ru", "login", "Oleg",
            LocalDate.of(1985, 2, 1), new HashSet<>());
    private final User emptyNameUser = new User(6L, "vanya2@yandex.ru", "login1", null,
            LocalDate.of(1985, 1, 1), new HashSet<>());
    private final User incorrectEmailUser = new User(3L, "email",
            "loglog", "Nekit", LocalDate.of(1995, 5, 42), new HashSet<>());
    private final User emptyEmailUser = new User(1L, "", "logt", null,
            LocalDate.of(1985, 1, 1), new HashSet<>());
    private final User commonFriend = new User(19L, "friend@yandex.ru", "friend", "Alexander",
            LocalDate.of(1995, 1, 11), new HashSet<>());

    @AfterEach
    public void afterEach() {
        storage.deleteUsers();
    }

    @Test
    void createUserTest() {
        controller.createUser(user);

        Assertions.assertEquals(1, controller.getUsers().size());
    }

    @Test
    void getUserByIdTest() {
        controller.createUser(user);
        User thisUser = storage.getUserById(user.getId());

        Assertions.assertEquals(thisUser.getId(), user.getId());
    }

    @Test
    void updateTest() {
        controller.createUser(user);
        controller.updateUser(updatedUser);

        Assertions.assertEquals("vanya1@yandex.ru", updatedUser.getEmail());
        Assertions.assertEquals(user.getId(), updatedUser.getId());
        Assertions.assertEquals(1, controller.getUsers().size());
    }

    @Test
    void createUserIsEmpty() {
        controller.createUser(emptyNameUser);

        Assertions.assertEquals(6, emptyNameUser.getId());
        Assertions.assertEquals("user", emptyNameUser.getName());
    }

    @Test
    void createUserEmailIsIncorrect() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(incorrectEmailUser));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void createUserEmailIsEmpty() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(emptyEmailUser));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void createUserLoginIsEmpty() {
        user.setLogin("");

        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void createUserFuture() {
        user.setBirthday(LocalDate.of(3333, 3, 11));

        Assertions.assertThrows(ValidationException.class, () -> controller.createUser(user));
        Assertions.assertEquals(0, controller.getUsers().size());
    }

    @Test
    void addFriendTest() {
        controller.createUser(user);
        controller.createUser(emptyNameUser);
        controller.addFriend(user.getId(), emptyNameUser.getId());

        Assertions.assertTrue(user.getFriendsQuantity() != 0);
        Assertions.assertTrue(emptyNameUser.getFriendsQuantity() != 0);
    }

    @Test
    void deleteFriendTest() {
        controller.createUser(user);
        controller.createUser(emptyNameUser);
        controller.addFriend(user.getId(), emptyNameUser.getId());
        controller.removeFriend(user.getId(), emptyNameUser.getId());

        Assertions.assertEquals(0, user.getFriendsQuantity());
        Assertions.assertEquals(0, emptyNameUser.getFriendsQuantity());
    }

    @Test
    void getCommonFriendsTest() {
        controller.createUser(user);
        controller.createUser(emptyNameUser);
        controller.addFriend(user.getId(), emptyNameUser.getId());
        controller.createUser(commonFriend);
        controller.addFriend(user.getId(), commonFriend.getId());
        controller.addFriend(emptyNameUser.getId(), commonFriend.getId());
        List<User> commonFriendList = controller.getCommonFriends(user.getId(), emptyNameUser.getId());

        Assertions.assertEquals(1, commonFriendList.size());
    }

    @Test
    void getFriendsTest() {
        controller.createUser(user);
        controller.createUser(emptyNameUser);
        controller.createUser(commonFriend);
        controller.addFriend(user.getId(), emptyNameUser.getId());
        controller.addFriend(user.getId(), commonFriend.getId());
        List<User> listOfUsersFriends = controller.getFriends(user.getId());

        Assertions.assertEquals(2, listOfUsersFriends.size());
    }

}
