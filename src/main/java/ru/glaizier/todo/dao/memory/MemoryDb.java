package ru.glaizier.todo.dao.memory;

import static ru.glaizier.todo.domain.Role.ADMIN;
import static ru.glaizier.todo.domain.Role.USER;

import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.Db;
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

// Just a simple memory db dummy
@Repository
public class MemoryDb implements Db {

    private final ConcurrentMap<String, ConcurrentMap<Integer, Task>> loginToIdToTask = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, User> loginToUser = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        addUser(User.builder().login("u").password("p".toCharArray())
                .roles(Collections.singletonList(USER)).build());
        addUser(User.builder().login("a").password("p".toCharArray())
                .roles(Arrays.asList(USER, ADMIN)).build());

        createTask("u", "todo1");
        createTask("u", "todo2");
        createTask("a", "todo1");
    }

    @Override
    public void addUser(User user) {
        loginToUser.put(user.getLogin(), user);
        loginToIdToTask.put(user.getLogin(), new ConcurrentHashMap<>());
    }

    @Override
    public User getUser(String login) {
        return loginToUser.get(login);
    }

    @Override
    public User removeUser(String login) {
        loginToIdToTask.remove(login);
        return loginToUser.remove(login);
    }

    @Override
    public Set<User> getUsers() {
        return new HashSet<>(loginToUser.values());
    }

    @Override
    public boolean containsLogin(String login) {
        return loginToIdToTask.containsKey(login);
    }


    @Override
    public Set<Task> getTasks(String login) {
        if (loginToIdToTask.get(login) == null)
            return null;
        return new HashSet<>(loginToIdToTask.get(login).values());
    }

    @Override
    public Task createTask(String login, String todo) {
        if (!containsLogin(login))
            return null;

        Integer newId = getLastId(login).orElse(0) + 1;
        Task newTask = new Task(newId, todo);

        ConcurrentMap<Integer, Task> idToTask = loginToIdToTask.get(login);
        if (idToTask == null)
            idToTask = new ConcurrentHashMap<>();
        idToTask.put(newId, newTask);
        loginToIdToTask.put(login, idToTask);

        return newTask;
    }

    @Override
    public Task getTask(String login, int id) {
        return (loginToIdToTask.get(login) == null) ? null : loginToIdToTask.get(login).get(id);
    }

    @Override
    public Task updateTask(String login, Task task) {
        if (loginToIdToTask.get(login) == null)
            return null;

        Task prevTask = loginToIdToTask.get(login).get(task.getId());
        if (prevTask != null) {
            Task updatedTask = new Task(task.getId(), task.getTodo());
            Map<Integer, Task> updatedMap = loginToIdToTask.get(login);
            updatedMap.put(updatedTask.getId(), updatedTask);
        }
        return prevTask;
    }

    @Override
    public Task removeTask(String login, int id) {
        if (loginToIdToTask.get(login) == null)
            return null;
        return loginToIdToTask.get(login).remove(id);
    }

    private Optional<Integer> getLastId(String login) {
        return loginToIdToTask.get(login).keySet().stream().max(Integer::compareTo);
    }
}