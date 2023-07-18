package ru.yandex.practicum.filmorate;

import exception.ValidationException;
import manager.FilmManager;
import model.Film;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmManagerTests {
    FilmManager filmManager = new FilmManager();
    Map<Integer, Film> films;

    @Test
    void testCreateFilm(Film film) throws ValidationException {
        Date date = new Date(121, 5, 7);
        Film film1 = new Film.FilmBuilder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(date)
                .duration(120)
                .build();
        Film createdFilm = filmManager.createFilm(film);assertEquals(1, createdFilm.getId(), "ID созданного фильма != 1");
        assertEquals("filmName", createdFilm.getName());
        assertEquals(1, films.size(), "размер мапы != 1");

    }

    @Test
    void doNotCreateFilmWithIncorrectName() {
        Date date = new Date(2021, 6, 7);
        Film film = new Film.FilmBuilder()
                .id(0)
                .description("some")
                .releaseDate(date)
                .duration(120)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmManager.createFilm(film)
        );

        assertEquals("🔹поле \"name\" должно быть заполнено!", exception.getMessage());
        assertEquals(0, films.size(), "размер мапы != 0");
    }
}
