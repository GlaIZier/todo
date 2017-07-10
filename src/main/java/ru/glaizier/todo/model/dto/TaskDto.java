package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class TaskDto {
    @NonNull
    private final Integer id;
    @NonNull
    private final Optional<UserDto> user;
    @NonNull
    private final String todo;
}
