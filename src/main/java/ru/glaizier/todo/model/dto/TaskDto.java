package ru.glaizier.todo.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class TaskDto {
    private final Integer id;
    private final String login;
    private final Set<String> roles;
    private final String todo;
}
