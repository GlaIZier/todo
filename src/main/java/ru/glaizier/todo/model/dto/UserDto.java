package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private final String login;
    private final char[] password;
    private final Set<RoleDto> roles;
}
