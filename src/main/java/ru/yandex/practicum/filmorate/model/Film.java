package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@Component
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private double duration;
    private Set<Integer> likes;
}