package ru.nikita.security.services;

import org.springframework.stereotype.Service;
import ru.nikita.security.exceptions.UserNotFoundException;
import ru.nikita.security.models.User;

import java.util.List;

@Service
public class UserService {

    private final List<User> users = List.of(
            User.builder()
                    .id(1L)
                    .firstName("James")
                    .lastName("Bond")
                    .age(35)
                    .build(),
            User.builder()
                    .id(2L)
                    .firstName("Ditta")
                    .lastName("Fon Tiss")
                    .age(43)
                    .build(),
            User.builder()
                    .id(3L)
                    .firstName("Kiso")
                    .lastName("Babu")
                    .age(18)
                    .build()
    );

    public List<User> getUsers() {
        return users;
    }

    public User getUser(Long userId) {
        return users
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(UserNotFoundException::new);
    }
}
