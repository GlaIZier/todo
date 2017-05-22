package ru.glaizier.todo.domain.api.input;

import lombok.Data;

@Data
public class InputUser {
    private final String login;
    private final char[] password;
}
