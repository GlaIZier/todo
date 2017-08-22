package ru.glaizier.todo.model.dto.api;

import lombok.Data;

@Data
public class HttpResponse {
    private final int code;
    private final String message;
}
