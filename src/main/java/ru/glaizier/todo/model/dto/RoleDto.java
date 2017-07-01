package ru.glaizier.todo.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class RoleDto {
    @NonNull
    private final String role;
}
