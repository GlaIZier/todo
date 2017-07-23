package ru.glaizier.todo.model.dto;

import lombok.*;

import java.util.Optional;
import java.util.Set;

@Data
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@Builder(toBuilder = true)
public class UserDto {
    @NonNull
    private final String login;
    @NonNull
    private final char[] password;
    @NonNull
    private final Optional<Set<RoleDto>> roles;
}
