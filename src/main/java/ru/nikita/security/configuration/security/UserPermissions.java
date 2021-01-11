package ru.nikita.security.configuration.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserPermissions {
    READ("READ"),
    WRITE("WRITE");

    private final String permission;
}
