package controller;

import exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import manager.FilmManager;
import model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    FilmManager filmManager = new FilmManager();

    @GetMapping("/all-film")
    public List<Film> getFilms() {
        List<Film> films = filmManager.getAllFilms();
        log.info("Выведен список фильмов " + films);
        return films;
    }

    @PutMapping("/add-film")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        try {
            Film updatedFilm = filmManager.updateFilm(film);
            return updatedFilm;
        } catch (ValidationException exception) {
            log.warn("Фильм не добавлен " + exception.getMessage());
            throw new ValidationException("Фильм не добавлен");
        }
    }

    @PostMapping("/post-film")
    public Film postFilm(@RequestBody Film film) throws ValidationException {
        try {
            Film newFilm = filmManager.createFilm(film);
            return newFilm;
        } catch (ValidationException exception) {
            log.warn("Фильм не обновлён " + exception.getMessage());
            throw new ValidationException("Фильм не обновлён");
        }
    }


    // ДОБАВЛЕНИЕ ФИЛЬМА
   // ОБНОВЛЕНИЕ ФИЛЬМА
   // ПОЛУЧЕНИЕ ВСЕХ ФИЛЬМОВ
}
