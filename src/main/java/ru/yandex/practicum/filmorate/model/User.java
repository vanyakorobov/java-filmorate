package ru.yandex.practicum.filmorate.model;

import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    @PositiveOrZero
    private Long id;
    @Email
    private String email;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends;
    @NotNull
    private String login;
    private String name;

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }

    public int getFriendsQuantity() {
        return friends.size();
    }
}
