package ru.glaizier.todo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.domain.Task;

public interface TaskDao extends JpaRepository<Task, Integer>/*, OverrideTaskDao */ {

//    List<Task> findTasksByLogin(String login);

//    Task findTaskByIdAndLogin(Integer id, String login);

    Task findTaskById(Integer id);

    <S extends Task> S save(S s);

    void delete(Integer integer);
}
