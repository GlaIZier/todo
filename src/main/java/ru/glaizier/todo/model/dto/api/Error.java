package ru.glaizier.todo.model.dto.api;

import lombok.Data;

@Data
public class Error {
    private final int code;
    private final String message;
}
