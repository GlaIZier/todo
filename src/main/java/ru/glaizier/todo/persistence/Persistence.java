package ru.glaizier.todo.persistence;

import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

import java.util.List;
import java.util.Set;

public interface Persistence {

    List<TaskDto> findTasksByLogin(String login);

    TaskDto findTaskById(Integer id);

    TaskDto saveTask(String login, String todo);

    TaskDto saveTask(String login, Integer id, String todo);

    void deleteTask(Integer id);

    UserDto findUserByLogin(String login);

    UserDto findUserByLoginAndPassword(String login, char[] password);

    UserDto saveUser(String login, char[] password, Set<RoleDto> roles);

    void deleteUser(String login);

    TaskDto findTaskByIdAndLogin(Integer id, String login) throws AccessDeniedException;

    RoleDto findRole(String role);

    RoleDto saveRole(String role);

    RoleDto updateRole(String role, String updatedRole);

    void deleteRole(String role);
}
