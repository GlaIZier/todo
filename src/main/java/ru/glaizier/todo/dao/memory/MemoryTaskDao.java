package ru.glaizier.todo.dao.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.Db;
import ru.glaizier.todo.dao.TaskDao;
import ru.glaizier.todo.domain.Task;

import java.util.Set;

@Repository
public class MemoryTaskDao implements TaskDao {

    private final Db db;

    @Autowired
    public MemoryTaskDao(Db db) {
        this.db = db;
    }

    @Override
    public Set<Task> getTasks(String login) {
        return db.getTasks(login);
    }

    @Override
    public Task createTask(String login, String todo) {
        return db.createTask(login, todo);
    }

    @Override
    public Task getTask(String login, int id) {
        return db.getTask(login, id);
    }

    @Override
    public Task updateTask(String login, Task task) {
        return db.updateTask(login, task);
    }

    @Override
    public Task removeTask(String login, int id) {
        return db.removeTask(login, id);
    }

    @Override
    public boolean containsLogin(String login) {
        return db.containsLogin(login);
    }

}
