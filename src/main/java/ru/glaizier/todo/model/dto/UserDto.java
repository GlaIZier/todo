package ru.glaizier.todo.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.glaizier.todo.model.domain.Role;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private final String login;
    private final char[] password;
    private final Set<Role> roles;
}
