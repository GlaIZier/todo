package ru.glaizier.todo.dao;

import ru.glaizier.todo.domain.Task;

import java.util.List;

public interface OverrideTaskDao {

    List<Task> findTasksByLogin(String login);

    Task findTaskByIdAndLogin(Integer id, String login);

    Task findTaskById(Integer id);

    <S extends Task> S save(S s);

    void delete(Integer integer);
}
