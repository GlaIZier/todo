package ru.glaizier.todo.persistence.memory.task;

import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;

import java.util.List;

public interface OverrideTaskDao {

//    List<Task> findTasksByLogin(String login);
//
//    Task findTaskByIdAndLogin(Integer id, String login);
//
//    Task findTaskById(Integer id);
//
//    <S extends Task> S save(S s);
//
//    void delete(Integer integer);

    List<Task> findAll();

    List<Task> findTasksByUser(User user);

    Task findTaskById(Integer id);

    Task findTaskByIdAndUser(Integer id, User user);

    <S extends Task> S save(S s);

    void delete(Integer integer);
}
