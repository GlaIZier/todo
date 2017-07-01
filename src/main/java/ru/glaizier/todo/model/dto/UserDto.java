package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

@Data
@Builder
public class UserDto {
    @NonNull
    private final String login;
    @NonNull
    private final char[] password;
    @NonNull
    private final Optional<Set<RoleDto>> roles;
}
