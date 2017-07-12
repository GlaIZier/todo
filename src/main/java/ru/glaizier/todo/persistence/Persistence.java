package ru.glaizier.todo.persistence;

import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

import java.util.List;
import java.util.Set;

public interface Persistence {

    List<TaskDto> findTasksByLogin(String login);

    TaskDto findTask(Integer id);

    TaskDto findTask(Integer id, String login) throws AccessDeniedException;

    TaskDto saveTask(String login, String todo);

    TaskDto updateTask(String login, Integer id, String todo) throws AccessDeniedException;

    TaskDto deleteTask(Integer id);

    TaskDto deleteTask(Integer id, String login) throws AccessDeniedException;

    UserDto findUser(String login);

    UserDto findUser(String login, char[] password);

    UserDto saveUser(String login, char[] password, Set<RoleDto> roles);

    void deleteUser(String login);

    RoleDto findRole(String role);

    RoleDto saveRole(String role);

    RoleDto updateRole(String role, String updatedRole);

    void deleteRole(String role);
}
