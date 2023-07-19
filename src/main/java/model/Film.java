package model;

import lombok.Data;
import java.util.Date;

@Data
public class Film {
    private int id;
    private  String name;
    private String description;
    private Date releaseDate;
    private int duration;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public static class FilmBuilder {

        private final Film film;

        public FilmBuilder() {
            film = new Film();
        }

        public FilmBuilder id(int id) {
            film.id = id;
            return this;
        }

        public FilmBuilder name(String name) {
            film.name = name;
            return this;
        }

        public FilmBuilder description(String description) {
            film.description = description;
            return this;
        }

        public FilmBuilder releaseDate(Date releaseDate) {
            film.releaseDate = releaseDate;
            return this;
        }

        public FilmBuilder duration(int duration) {
            film.duration = duration;
            return this;
        }

        public Film build() {
            return film;
        }

    }

}
