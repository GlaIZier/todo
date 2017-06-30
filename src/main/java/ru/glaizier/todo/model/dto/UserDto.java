package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private final String login;
    private final char[] password;
    private final Optional<Set<RoleDto>> roles;
}
