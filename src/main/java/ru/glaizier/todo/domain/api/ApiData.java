package ru.glaizier.todo.domain.api;

import lombok.Value;

@Value
public class ApiData<T> {
    private final T data;
}
