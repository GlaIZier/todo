package ru.glaizier.todo.dao.embedded;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.domain.Task;

public interface EmbeddedTaskDao extends JpaRepository<Task, Integer> {

    Task findTaskById(int id);

    Task findTaskByIdAndLogin(int id, String login);

}
