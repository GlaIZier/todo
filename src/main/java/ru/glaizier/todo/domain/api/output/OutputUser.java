package ru.glaizier.todo.domain.api.output;

import lombok.Data;

@Data
public class OutputUser {
    private final String login;
    private final String token;
}
