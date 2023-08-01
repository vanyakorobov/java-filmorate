package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionForResponse;
import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private InMemoryFilmStorage inMemoryFilmStorage;
    private FilmService filmService;
    public void filmService(FilmService filmService, InMemoryFilmStorage inMemoryFilmStorage) {
        this.filmService = filmService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping()
    public List<Film> getFilmsList() {
        List<Film> films = inMemoryFilmStorage.getFilmsList();
        log.info("выдан список фильмов: " + films);
        return films;
    }

    @GetMapping("{filmId}")
    public Film getFilmFromTheId(@PathVariable int filmId) {
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    @GetMapping("/topFilms")
    public List<Film> topFilms() {
        return filmService.getTopLikedFilms();
    }

    @PostMapping
    public Film createUser(@RequestBody Film newFilm) throws ValidationException, ValidationExceptionForResponse {
        try {
            Film createdFilm = inMemoryFilmStorage.createFilm(newFilm);
            log.info("фильм добавлен: " + createdFilm);
            return createdFilm;
        } catch (ValidationException e) {
            log.warn("фильм не добавлен" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException, ValidationExceptionForResponse {
        try {
            Film currentFilm = inMemoryFilmStorage.updateFilm(updatedFilm);
            log.info("обновлён фильм: " + currentFilm);
            return currentFilm;
        } catch (ValidationException e) {
            log.info("фильм не обновлён");
            log.warn(e.getMessage());
            System.out.println(e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping("{userId}")
    public int addLikes(@PathVariable int userId) {
        filmService.addLike(userId);
        return userId;
    }

    @DeleteMapping("{userId}")
    public int deleteLike(@PathVariable int userId) {
        filmService.removeLike(userId);
        return userId;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNegativeId(final IllegalArgumentException e) {
        return Map.of("error", "Передан неверный id");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNullId(final NullPointerException e) {
        return Map.of("error", "Не передан id");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        return Map.of("error", "Введены неверные данные");
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(RuntimeException ex) {
        return "Страница не найдена: " + ex.getMessage();
    }
}
