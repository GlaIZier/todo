package ru.glaizier.todo.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@EqualsAndHashCode(exclude = {"password", "roles"})
public class User {
    private final String login;
    private final char[] password;
    private final List<Role> roles;
}
