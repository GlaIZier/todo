package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private final Integer id;
    private final UserDto user;
    private final String todo;
}
