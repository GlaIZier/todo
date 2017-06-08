package ru.glaizier.todo.dao.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.Db;
import ru.glaizier.todo.dao.TaskDao;
import ru.glaizier.todo.domain.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Repository
public class MemoryTaskDao implements TaskDao {

    private final Db db;

    @Autowired
    public MemoryTaskDao(Db db) {
        this.db = db;
    }

    @Override
    public List<Task> getTasks(String login) {
        Set<Task> tasks = db.getTasks(login);
        if (tasks == null)
            return null;
        return tasks.stream().sorted(Comparator.comparingInt(Task::getId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
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
