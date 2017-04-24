package ru.glaizier.todo.domain;

import lombok.Value;

@Value
public class Error {
    private final int code;
    private final String message;
}
