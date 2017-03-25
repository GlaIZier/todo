package ru.glaizier.dao;

import org.springframework.stereotype.Repository;
import ru.glaizier.domain.Task;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemoryTaskDao implements TaskDao {

    private final Map<Integer, Task> idToTask = new HashMap<>();

    private int lastId = 0;

    @PostConstruct
    private void init() {
        idToTask.put(1, new Task(1, "todo1", null));
        idToTask.put(2, new Task(2, "todo2", null));

        lastId = 2;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(idToTask.values());
    }

    @Override
    public Task createTask(String todo) {
        Task task = new Task(lastId, "todo" + lastId, null);
        idToTask.put(++lastId, task);
        return task;
    }

    @Override
    public Task getTask(int id) {
        return idToTask.get(id);
    }

    @Override
    public Task updateTask(int id, String todo) {
        Task task = idToTask.get(id);
        if (task == null)
            return null;
        task.setTodo(todo);
        idToTask.put(id, task);
        return task;
    }

    @Override
    public Task removeTask(int id) {
        return idToTask.remove(id);
    }
}
