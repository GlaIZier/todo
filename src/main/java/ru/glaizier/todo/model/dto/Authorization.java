package ru.glaizier.todo.model.dto;

import lombok.Data;

@Data
public class Authorization {
    private final String login;
    private final String role;
}
