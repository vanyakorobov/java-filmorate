package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {

    private InMemoryFilmStorage inMemoryFilmStorage;
    private Film film;

    @Autowired
    public void filmService(Film film, InMemoryFilmStorage inMemoryFilmStorage) {
        this.film = film;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(int id) {
        film.getLikes().add(id);
        Film like = inMemoryFilmStorage.getFilmById(id);
        like.getLikes().add(film.getId());
    }

    public void removeLike(int id) {
        film.getLikes().remove(id);
        Film like = inMemoryFilmStorage.getFilmById(id);
        like.getLikes().remove(film.getId());
    }

    public List<Film> getTopLikedFilms() {
        List<Film> films = inMemoryFilmStorage.getFilmsList();

        films.sort(Comparator.comparingInt(f -> f.getLikes().size()));
        Collections.reverse(films);

        List<Film> topLikedFilms = new ArrayList<>();
        int count = Math.min(10, films.size());

        for (int i = 0; i < count; i++) {
            topLikedFilms.add(films.get(i));
        }

        return topLikedFilms;
    }

}
