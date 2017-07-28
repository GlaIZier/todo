package ru.glaizier.todo.model.dto;

import lombok.Data;
import lombok.NonNull;
import ru.glaizier.todo.model.domain.Role;

@Data
public class RoleDto {
    public static RoleDto USER = new RoleDto(Role.USER.getRole());
    public static RoleDto ADMIN = new RoleDto(Role.ADMIN.getRole());
    @NonNull
    private final String role;
}
