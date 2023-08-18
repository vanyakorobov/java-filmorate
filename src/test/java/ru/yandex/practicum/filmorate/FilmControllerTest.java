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

public class FilmControllerTest {
    private FilmStorage storage = new InMemoryFilmStorage();
    private UserStorage userStorage = new InMemoryUserStorage();
    private UserService userService = new UserService(userStorage);
    private FilmService service = new FilmService(userService);
    private FilmController controller = new FilmController(service);
    private final Film negativeDurationFilm = new Film(1L, "Movie",
            "Описание фильма",
            LocalDate.of(1995, 2, 2), -15, new HashSet<>());
    private final User user = new User(2L, "vanya@ya.ru", "vanya", "Vanya",
            LocalDate.of(1999, 3, 5), new HashSet<>());
    private final Film film = new Film(1L, "Film", "Описание фильма",
            LocalDate.of(1995, 2, 2), 120, new HashSet<>());
    private final Film updatedFilm = new Film(1L, "Film",
            "Описание этого фильма",
            LocalDate.of(1995, 2, 2), 120, new HashSet<>());
    private final Film noNamedFilm = new Film(1L, "", "Описание фильма",
            LocalDate.of(1995, 2, 2), 120, new HashSet<>());
    private final Film longDescpriptionFilm = new Film(1L, "Film",
            "Жесть какая-то, а не фильм",
            LocalDate.of(1995, 2, 2), 120, new HashSet<>());

    @AfterEach
    public void afterEach() {
        storage.deleteFilms();
    }

    @Test
    void createFilmTest() {
        controller.createFilm(film);

        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void updateFilmTest() {
        controller.createFilm(film);
        controller.updateFilm(updatedFilm);

        Assertions.assertEquals("Описание этого фильма", updatedFilm.getDescription());
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void createFilmEmptyNameTest() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(noNamedFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void getFilmByIdTest() {
        controller.createFilm(film);
        Film thisFilm = controller.getFilmById(film.getId());

        Assertions.assertEquals(1, thisFilm.getId());
    }

    @Test
    void createFilmDuration0Test() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(negativeDurationFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm1895Test() {
        film.setReleaseDate(LocalDate.of(1895, 2, 2));

        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }
}