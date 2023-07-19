package manager;

import exception.ValidationException;
import model.Film;

import java.util.*;

public class FilmManager {
    private static int filmId;
    private Map<Integer, Film> films = new HashMap();

    public Film createFilm(Film film) throws ValidationException {
        nameValidation(film.getName());
        descriptionValidation(film.getDescription());
        dateValidation(film.getReleaseDate());
        durationValidation(film.getDuration());
        int newId = generateId();
        films.put(newId, film);
        return film;
    }

    public Film updateFilm(Film updateFilm) throws ValidationException {
        int id = updateFilm.getId();
        nameValidation(updateFilm.getName());
        descriptionValidation(updateFilm.getDescription());
        dateValidation(updateFilm.getReleaseDate());
        durationValidation(updateFilm.getDuration());
        films.put(id, updateFilm);
        return updateFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>(films.values());
        return allFilms;
    }

    public int generateId() {
        return ++ filmId;
    }

    public void nameValidation(String name) throws ValidationException {
        if (name != null) {
            if (name.isBlank()) {
                throw new ValidationException("Имя не может быть пустым");
            }
        }
    }

    public void  descriptionValidation(String description) throws ValidationException {
        if (description.length() > 200) {
            throw new ValidationException("Описание не должно превышать 200 символов");
        }
    }

    public void dateValidation(Date date) throws ValidationException {
        Date startDate = new Date(95, 11, 28);
        if (date.before(startDate)) {
            throw new ValidationException("Фильм не мог быть снят раньше, чем 28 декабря 1895 г.");
        }
    }

    public void durationValidation(int duration) throws ValidationException {
        if (duration < 0) {
            throw new ValidationException("родолжительность фильма не может быть отрицательной");
        }
    }
}
