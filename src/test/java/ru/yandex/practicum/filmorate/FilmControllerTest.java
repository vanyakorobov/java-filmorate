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
    private final Film film = new Film(1L, "Movie", "The most awesome movie I've ever seen",
            LocalDate.of(2020, 2, 2), 120, new HashSet<>());
    private final Film updatedFilm = new Film(1L, "Movie",
            "I cried at the end, it was very thoughtful",
            LocalDate.of(2020, 2, 2), 120, new HashSet<>());
    private final Film noNamedFilm = new Film(1L, "", "The most awesome movie I've ever seen",
            LocalDate.of(2020, 2, 2), 120, new HashSet<>());
    private final Film longDescpriptionFilm = new Film(1L, "Movie",
            "This is the most amazing and terrifying movie in my life. I love scary movies," +
                    "but I've never seen such precise details of serial killers doing their job." +
                    "You should definitely see this one. Actually, this movie was based on a true story. Creepy...",
            LocalDate.of(2020, 2, 2), 120, new HashSet<>());
    private final Film negativeDurationFilm = new Film(1L, "Movie",
            "The most awesome movie I've ever seen",
            LocalDate.of(2020, 2, 2), -15, new HashSet<>());
    private final User user = new User(2L, "lollipop@ya.ru", "lollipop", "Martin",
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
    void updateFilm_shouldUpdateMovieData() {
        controller.createFilm(film);
        controller.updateFilm(updatedFilm);

        Assertions.assertEquals("I cried at the end, it was very thoughtful", updatedFilm.getDescription());
        Assertions.assertEquals(1, controller.getFilms().size());
    }

    @Test
    void getFilmById_shouldReturnAMovieWithIdOne() {
        controller.createFilm(film);
        Film thisFilm = controller.getFilmById(film.getId());

        Assertions.assertEquals(1, thisFilm.getId());
    }

    @Test
    void createFilm_shouldNotAddAMovieWithAnEmptyName() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(noNamedFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm_shouldNotAddAMovieWithDescriptionMoreThan200() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(longDescpriptionFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm_shouldNotAddAMovieWithDateReleaseLessThan1895() {
        film.setReleaseDate(LocalDate.of(1891, 2, 2));

        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(film));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void createFilm_shouldNotAddAMovieIfDurationIsLessThan0() {
        Assertions.assertThrows(ValidationException.class, () -> controller.createFilm(negativeDurationFilm));
        Assertions.assertEquals(0, controller.getFilms().size());
    }

    @Test
    void likeAMovie_shouldAddALikeToAMovie() {
        userStorage.createUser(user);
        controller.createFilm(film);
        controller.addLike(film.getId(), user.getId());

        Assertions.assertTrue(film.getLikesQuantity() != 0);
    }

    @Test
    void removeLike_shouldRemoveLikeFromAMovie() {
        userStorage.createUser(user);
        controller.createFilm(film);
        controller.addLike(film.getId(), user.getId());
        controller.removeLike(film.getId(), user.getId());

        Assertions.assertEquals(0, film.getLikesQuantity());
    }

    @Test
    void getPopularMovies_shouldReturnListOfPopularMovies() {
        userStorage.createUser(user);
        controller.createFilm(film);
        controller.addLike(film.getId(), user.getId());
        List<Film> popularMoviesList = service.getPopularFilms(1);

        Assertions.assertEquals(1, popularMoviesList.size());
    }
}