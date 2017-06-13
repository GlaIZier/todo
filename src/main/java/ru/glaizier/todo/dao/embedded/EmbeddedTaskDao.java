package ru.glaizier.todo.dao.embedded;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.domain.Task;

import java.util.List;

public interface EmbeddedTaskDao extends JpaRepository<Task, Integer> {

    List<Task> findTasksByLogin(String login);

    Task findTaskByIdAndLogin(int id, String login);
}
