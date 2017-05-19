package ru.glaizier.todo.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class User {
    private final String login;
    private final char[] password;
    private final List<Role> roles;
}
