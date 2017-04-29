package ru.glaizier.todo.dao;

import org.springframework.stereotype.Repository;
import ru.glaizier.todo.domain.Task;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class MemoryTaskDao implements TaskDao {

    private final Map<String, Map<Integer, Task>> loginToIdToTasks = new HashMap<>();

    @PostConstruct
    private void init() {
        Map<Integer, Task> idToTask = new HashMap<>();
        idToTask.put(1, new Task(1, "todo1"));
        idToTask.put(2, new Task(2, "todo2"));
        loginToIdToTasks.put("u", idToTask);

        idToTask = new HashMap<>();
        idToTask.put(1, new Task(1, "todo1"));
        loginToIdToTasks.put("a", idToTask);
    }

    private Optional<Integer> getLastId(String login) {
        return loginToIdToTasks.get(login).keySet().stream().max(Integer::compareTo);
    }

    @Override
    public List<Task> getTasks(String login) {
        if (loginToIdToTasks.get(login) == null)
            return null;
        return new ArrayList<>(loginToIdToTasks.get(login).values());
    }

    @Override
    public Task createTask(String login, Task task) {
        Integer newId = getLastId(login).orElse(0) + 1;
        Task newTask = new Task(newId, task.getTodo());

        Map<Integer, Task> idToTask = loginToIdToTasks.get(login);
        if (idToTask == null)
            idToTask = new HashMap<>();
        idToTask.put(newId, newTask);
        loginToIdToTasks.put(login, idToTask);

        return newTask;
    }

    @Override
    public Task getTask(String login, int id) {
        return (loginToIdToTasks.get(login) == null) ? null : loginToIdToTasks.get(login).get(id);
    }

    @Override
    public Task updateTask(String login, Task task) {
        if (loginToIdToTasks.get(login) == null)
            return null;

        Task prevTask = loginToIdToTasks.get(login).get(task.getId());
        if (prevTask != null) {
            Task updatedTask = new Task(task.getId(), task.getTodo());
            Map<Integer, Task> updatedMap = loginToIdToTasks.get(login);
            updatedMap.put(updatedTask.getId(), updatedTask);
        }
        return prevTask;
    }

    @Override
    public Task removeTask(String login, int id) {
        if (loginToIdToTasks.get(login) == null)
            return null;
        return loginToIdToTasks.get(login).remove(id);
    }

    @Override
    public boolean containsLogin(String login) {
        return loginToIdToTasks.containsKey(login);
    }

}
