package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
@Builder
public class TaskDto {
    private final Integer id;
    private final Optional<UserDto> user;
    private final String todo;
}
