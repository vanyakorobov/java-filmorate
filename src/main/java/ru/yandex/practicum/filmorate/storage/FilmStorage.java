package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film newFilm);

    Film updateFilm(Film updatedFilm);

    void nameValidation(String name);

    void descriptionValidation(String description);

    void releaseDateValidation(LocalDate releaseDate);

    void durationValidation(double duration);

    List<Film> getFilmsList();

    Film getFilmById(int currentID);

    int createID();
}
