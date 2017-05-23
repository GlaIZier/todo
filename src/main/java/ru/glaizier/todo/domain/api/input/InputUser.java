package ru.glaizier.todo.domain.api.input;

import lombok.Data;

@Data
public class InputUser {
    private String login;
    private char[] password;
}
