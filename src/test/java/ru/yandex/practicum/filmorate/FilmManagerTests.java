package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.FilmsManager;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("FilmManagerTests должен ")
public class FilmManagerTests {
    private FilmsManager filmsManager;
    private Map<Integer, Film> films;

    @BeforeEach
    public void createUserManager() {
        filmsManager = new FilmsManager();
        films = filmsManager.getFilms();
    }

    @AfterEach
    public void clearFilmManager() {
        films.clear();
        filmsManager.setCurrentID(0);
    }

    @DisplayName("создать фильм")
    @Test
    void createFilm() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();
        Film createdFilm = filmsManager.createFilm(film);

        assertEquals(1, createdFilm.getId(), "ID созданного фильма != 1");
        assertEquals("filmName", createdFilm.getName());
        assertEquals(1, films.size(), "размер мапы != 1");
    }

    @DisplayName("НЕ создавать фильм, если name=null")
    @Test
    void doNotCreateFilmWithIncorrectName() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmsManager.createFilm(film)
        );

        assertEquals("🔹поле \"name\" должно быть заполнено!", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }

    @DisplayName("НЕ создавать фильм, если description.length()>200")
    @Test
    void doNotCreateFilmWithIncorrectDescription() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(releaseDate)
                .duration(120)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmsManager.createFilm(film)
        );

        assertEquals("🔹длина description больше 200 символов!", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }

    @DisplayName("НЕ создавать фильм, если releaseDate раньше, чем 1885/12/28")
    @Test
    void doNotCreateFilmWithIncorrectReleaseDate() {
        LocalDate releaseDate = LocalDate.of(1884, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmsManager.createFilm(film)
        );

        assertEquals("🔹\"releaseDate\" не может быть раньше, чем 1895/12/28", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }

    @DisplayName("НЕ создавать фильм, если duration<0")
    @Test
    void doNotCreateFilmWithIncorrectDuration() {
        LocalDate releaseDate = LocalDate.of(1984, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(-1)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmsManager.createFilm(film)
        );

        assertEquals("🔹поле \"duration\" не может быть отрицательным или равно нулю!", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }
}