package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int currentID;
    private Map<Integer, Film> films = new HashMap();

    public Film createFilm(Film newFilm) throws ValidationException, NullPointerException {
        nameValidation(newFilm.getName());
        descriptionValidation(newFilm.getDescription());
        releaseDateValidation(newFilm.getReleaseDate());
        durationValidation(newFilm.getDuration());
        newFilm.setId(createID());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException {
        int id = updatedFilm.getId();
        if (films.containsKey(updatedFilm.getId())) {
            films.put(updatedFilm.getId(), updatedFilm);
        }
        nameValidation(updatedFilm.getName());
        descriptionValidation(updatedFilm.getDescription());
        releaseDateValidation(updatedFilm.getReleaseDate());
        durationValidation(updatedFilm.getDuration());

        films.put(id, updatedFilm);
        return updatedFilm;
    }

    public void nameValidation(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("поле \"name\" должно быть заполнено!");
        }
    }

    public void descriptionValidation(String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("некорректный description");
        }
        if (description.length() > 200) {
            throw new ValidationException("длина description больше 200 символов!");
        }

    }

    public void releaseDateValidation(LocalDate releaseDate) {
        if (releaseDate == null || releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("\"releaseDate\" не может быть пустым или раньше, чем 1895/12/28");
        }
    }

    public void durationValidation(double duration) {
        if (duration < 0.1) {
            throw new ValidationException("поле \"duration\" не может быть отрицательным или равно нулю!");
        }
    }

    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    public Film getFilmById(int currentID) {
        Film film = films.get(currentID);
        return film;
    }

    public int createID() {
        return ++currentID;
    }
}