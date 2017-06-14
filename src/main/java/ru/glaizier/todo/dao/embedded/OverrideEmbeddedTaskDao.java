package ru.glaizier.todo.dao.embedded;

import ru.glaizier.todo.domain.Task;

import java.util.List;

public interface OverrideEmbeddedTaskDao {

    List<Task> findTasksByLogin(String login);

    Task findTaskByIdAndLogin(int id, String login);

}
