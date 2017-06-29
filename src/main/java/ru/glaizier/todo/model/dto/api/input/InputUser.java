package ru.glaizier.todo.model.dto.api.input;

import lombok.Data;

@Data
// Todo remove InputUser and use User?
public class InputUser {
    private String login;
    private char[] password;
}
