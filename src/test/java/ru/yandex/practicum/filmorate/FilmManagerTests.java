package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmManagerTests {
    private InMemoryFilmStorage inMemoryFilmStorage;
    private List<Film> films;

    @BeforeEach
    public void createUserManager() {
        inMemoryFilmStorage = new InMemoryFilmStorage();
        films = inMemoryFilmStorage.getFilmsList();
    }

    @Test
    void createFilm() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmNam")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();
        Film createdFilm = inMemoryFilmStorage.createFilm(film);

        assertEquals(1, createdFilm.getId(), "ID созданного фильма != 1");
        assertEquals("filmName", createdFilm.getName());
    }

    @Test
    void correctName() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.createFilm(film)
        );
        assertEquals(0, films.size(), "размер мапы != 0");
    }

    @Test
    void correctDescription() {
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
                () -> inMemoryFilmStorage.createFilm(film)
        );

        assertEquals("длина description больше 200 символов!", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }

    @Test
    void correctReleaseDate() {
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
                () -> inMemoryFilmStorage.createFilm(film)
        );

        assertEquals("\"releaseDate\" не может быть пустым или раньше, чем 1895/12/28", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }

    @Test
    void correctDuration() {
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
                () -> inMemoryFilmStorage.createFilm(film)
        );

        assertEquals("поле \"duration\" не может быть отрицательным или равно нулю!", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }
}