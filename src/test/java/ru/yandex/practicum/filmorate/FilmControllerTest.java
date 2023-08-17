package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

public class FilmControllerTest {
    private FilmStorage storage = new InMemoryFilmStorage();
    private UserStorage userStorage = new InMemoryUserStorage();
    private UserService userService = new UserService(userStorage);
    private FilmService service = new FilmService(storage, userService);
    private FilmController controller = new FilmController(storage, service);
    private final Film film = new Film(1L, "Film", "Описание фильма",
            LocalDate.of(1985, 1, 1), 111, new HashSet<>());
    private final Film updatedFilm = new Film(1L, "Film",
            "Описание фильма",
            LocalDate.of(1985, 1, 1), 111, new HashSet<>());
    private final Film noNamedFilm = new Film(1L, "", "Описание фильма",
            LocalDate.of(1985, 1, 1), 111, new HashSet<>());
    private final Film longDescpriptionFilm = new Film(1L, "Film",
            "Описание этого фильма",
            LocalDate.of(2020, 2, 2), 120, new HashSet<>());
    private final Film negativeDurationFilm = new Film(1L, "Film",
            "Описание фильма",
            LocalDate.of(2020, 2, 2), -15, new HashSet<>());
    private final User user = new User(2L, "vanya@ya.ru", "vanya", "vanya",
            LocalDate.of(1997, 3, 5), new HashSet<>());

    @AfterEach
    public void afterEach() {
        storage.deleteFilms();
    }

    @Test
    void createFilm_shouldAddAMovie() {
        controller.createFilm(film);

        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void updateFilmTest() {
        controller.createFilm(film);
        controller.updateFilm(updatedFilm);

        Assertions.assertEquals("жесть какая-то", updatedFilm.getDescription());
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void getFilmByIdTest() {
        controller.createFilm(film);
        Film thisFilm = controller.getFilmById(film.getId());

        Assertions.assertEquals(1, thisFilm.getId());
    }

    @Test
    void createFilmTest() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(noNamedFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm200() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(longDescpriptionFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm1895() {
        film.setReleaseDate(LocalDate.of(1891, 2, 2));

        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm0() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(negativeDurationFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void likeAMovieTest() {
        userStorage.createUser(user);
        controller.createFilm(film);
        controller.addLike(film.getId(), user.getId());

        Assertions.assertTrue(film.getLikesQuantity() != 0);
    }

    @Test
    void removeLikeTest() {
        userStorage.createUser(user);
        controller.createFilm(film);
        controller.addLike(film.getId(), user.getId());
        controller.removeLike(film.getId(), user.getId());

        Assertions.assertEquals(0, film.getLikesQuantity());
    }

    @Test
    void getPopularMoviesTest() {
        userStorage.createUser(user);
        controller.createFilm(film);
        controller.addLike(film.getId(), user.getId());
        List<Film> popularMoviesList = service.getPopularFilms(1);

        Assertions.assertEquals(1, popularMoviesList.size());
    }
}