package ru.glaizier.todo.persistence.memory;

import lombok.NonNull;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

// Just a simple memory db dummy
// Todo create simple memory implementation
//@Repository
//@Profile("memory")
public class MemoryDb implements Db {

    private final ConcurrentMap<String, ConcurrentMap<Integer, Task>> loginToIdToTask = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, User> loginToUser = new ConcurrentHashMap<>();

    private AtomicInteger lastId = new AtomicInteger(0);

    @PostConstruct
    public void init() {
//        addUser(User.builder().login("u").password("p".toCharArray())
//                /*.roles(Collections.singletonList(USER))*/.build());
//        addUser(User.builder().login("a").password("p".toCharArray())
//                /*.roles(Arrays.asList(USER, ADMIN))*/.build());
//
//        createTask("u", "todo1");
//        createTask("u", "todo2");
//        createTask("a", "todo1");
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

        Integer newId = lastId.incrementAndGet();
//        Task newTask = Task.builder().id(newId).login(login).todo(todo).build();
        Task newTask = null;

        ConcurrentMap<Integer, Task> idToTask = loginToIdToTask.get(login);
        if (idToTask == null)
            idToTask = new ConcurrentHashMap<>();
        idToTask.put(newId, newTask);
        loginToIdToTask.put(login, idToTask);

        return newTask;
    }

    @Override
    public Task getTask(@NonNull Integer id) {
        for (ConcurrentMap<Integer, Task> map : loginToIdToTask.values()) {
            if (map.containsKey(id))
                return map.get(id);
        }
        return null;
    }

    @Override
    public Task getTask(String login, @NonNull Integer id) {
        return (loginToIdToTask.get(login) == null) ? null : loginToIdToTask.get(login).get(id);
    }

    @Override
    public Task updateTask(String login, Task task) {
        if (loginToIdToTask.get(login) == null)
            return null;

        Task prevTask = loginToIdToTask.get(login).get(task.getId());
        if (prevTask != null) {
//            Task updatedTask = Task.builder().id(task.getId()).login(login).todo(task.getTodo()).build();
            Task updatedTask = null;
            Map<Integer, Task> updatedMap = loginToIdToTask.get(login);
            updatedMap.put(updatedTask.getId(), updatedTask);
        }
        return prevTask;
    }

    @Override
    public Task removeTask(String login, @NonNull Integer id) {
        if (loginToIdToTask.get(login) == null)
            return null;
        return loginToIdToTask.get(login).remove(id);
    }

    @Override
    public Task removeTask(@NonNull Integer id) {
        for (ConcurrentMap<Integer, Task> map : loginToIdToTask.values()) {
            if (map.containsKey(id))
                return map.remove(id);
        }
        return null;
    }

    // Previously last id was for each login
//    private Optional<Integer> getLastId(String login) {
//        return loginToIdToTask.get(login).keySet().stream().max(Integer::compareTo);
//    }
}
