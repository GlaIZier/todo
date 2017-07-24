package ru.glaizier.todo.model.dto.api.input;

import lombok.Data;

@Data
public class InputUser {
    private String login;
    private char[] password;
}
