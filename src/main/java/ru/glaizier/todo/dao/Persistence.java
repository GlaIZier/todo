package ru.glaizier.todo.dao;

import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface Persistence {

    List<TaskDto> findTasksByUser(User user);

    TaskDto findTaskById(Integer id);

    TaskDto saveTask(String login, String todo);

    void deleteTask(Integer id);

    UserDto findUserByLogin(String login);

    UserDto findUserByLoginAndPassword(String login, char[] password);

    UserDto saveUser(String login, char[] password, Set<RoleDto> roles);

    void deleteUser(String login);

    TaskDto findTaskByIdAndLogin(Integer id, String login) throws AccessDeniedException;
}
