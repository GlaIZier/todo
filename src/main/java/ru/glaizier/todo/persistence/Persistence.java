package ru.glaizier.todo.persistence;

import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

import java.util.List;
import java.util.Set;

public interface Persistence {

    // All methods return null when login wasn't found
    // and throw exception in case of errors or security problems

    void initDb();

    List<TaskDto> findTasks(String login);

    TaskDto findTask(Integer id);

    TaskDto findTask(Integer id, String login) throws AccessDeniedException;

    TaskDto saveTask(String login, String todo);

    TaskDto updateTask(String login, Integer id, String todo) throws AccessDeniedException;

    TaskDto deleteTask(Integer id);

    TaskDto deleteTask(Integer id, String login) throws AccessDeniedException;

    List<UserDto> findUsers();

    UserDto findUser(String login);

    UserDto findUser(String login, char[] rawPassword);

    // Also is used to update user
    UserDto saveUser(String login, char[] rawPassword, Set<RoleDto> roles);

    // Save user with role "ROLE_USER"
    UserDto saveUser(String login, char[] rawPassword);

    void deleteUser(String login);

    List<RoleDto> findRoles();

    RoleDto findRole(String role);

    RoleDto saveRole(String role);

    // no update because we need to update all users with updated role

    void deleteRole(String role);
}
