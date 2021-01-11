package ru.nikita.security.configuration.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum UserRoles {
    ADMIN(Set.of(UserPermissions.WRITE, UserPermissions.READ)),
    OBSERVER(Set.of(UserPermissions.READ)),
    USER(Set.of(UserPermissions.READ));

    private final Set<UserPermissions> permissions;

}
