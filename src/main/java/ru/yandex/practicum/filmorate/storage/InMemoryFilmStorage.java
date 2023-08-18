package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films;
    private Long id;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
        id = 0L;
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        films.put(film.getId(), film);
        log.info("фильм создан", film.getName(), film.getId());
        return film;
    }

    @Override
    public void deleteFilms() {
        films.clear();
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.info("фильм обновлён", film.getName(), film.getId());
            return film;
        } else {
            throw new ObjectNotFoundException("Такого фильма не существует");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        if (!films.containsKey(id)) {
            throw new ObjectNotFoundException("Фильма с таким id не существует" + id + "'");
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validate(Film film) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата");
        }
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Отсутствует название фильма");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма не может быть меньше нуля");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() == 0) {
            throw new ValidationException("Максимальное количество символов 200");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }
}