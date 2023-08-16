package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private String email;
    private int id;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    final private Set<Integer> friends = new HashSet<>();
}
