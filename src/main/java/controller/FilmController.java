package controller;

import exception.ValidationException;
import exception.ValidationExceptionForResponse;
import lombok.extern.slf4j.Slf4j;
import manager.FilmsManager;
import manager.Managers;
import model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmsManager filmsManager = Managers.getDefaultFilmsManager();

    @GetMapping
    public List<Film> getFilmsList() {
        List<Film> films = filmsManager.getFilmsList();
        log.info("🟩 список фильмов выдан: " + films);
        return films;
    }

    @PostMapping
    public Film createUser(@RequestBody Film newFilm) throws ValidationException, ValidationExceptionForResponse {
        try {
            Film createdFilm = filmsManager.createFilm(newFilm);
            log.info("🟩 добавлен фильм: " + createdFilm);
            return createdFilm;
        } catch (ValidationException e) {
            log.info("🟩 фильм НЕ добавлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException, ValidationExceptionForResponse {
        try {
            Film currentFilm = filmsManager.updateFilm(updatedFilm);
            log.info("🟩 фильм обновлен: " + currentFilm);
            return currentFilm;
        } catch (ValidationException e) {
            log.info("🟩 фильм НЕ обновлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }
}
