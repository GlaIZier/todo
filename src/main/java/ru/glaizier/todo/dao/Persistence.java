package ru.glaizier.todo.dao;

import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;

import java.util.List;

public interface Persistence {

    List<TaskDto> findTasksByUser(User user);

    TaskDto findTaskById(Integer id);

    <S extends TaskDto> S saveTask(S s);

    void deleteTask(Integer integer);

    UserDto findUserByLogin(String login);

    UserDto findUserByLoginAndPassword(String login, char[] password);

    <S extends UserDto> S save(S s);

    void deleteUser(String login);

    TaskDto findTaskByIdAndLogin(Integer id, String login) throws AccessDeniedException;
}
