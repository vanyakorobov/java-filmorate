package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    void deleteFilms();

    Film getFilmById(Long id);

    Film updateFilm(Film film);

    List<Film> getFilms();

}
