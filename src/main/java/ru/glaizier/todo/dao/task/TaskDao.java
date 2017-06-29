package ru.glaizier.todo.dao.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;

import java.util.List;

public interface TaskDao extends JpaRepository<Task, Integer>/*, OverrideTaskDao */ {

//    List<Task> findTasksByLogin(String login);

//    Task findTaskByIdAndLogin(Integer id, String login);

    List<Task> findTasksByUser(User user);

    Task findTaskById(Integer id);

    <S extends Task> S save(S s);

    void delete(Integer integer);
}
