package ru.glaizier.todo.domain.api.input;

import lombok.Data;

@Data
// Todo remove InputUser and use User?
public class InputUser {
    private String login;
    private char[] password;
}
