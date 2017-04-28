package ru.glaizier.todo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class Task {
    private final int id;
    private final String todo;
}
