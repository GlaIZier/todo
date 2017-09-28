package ru.glaizier.todo.persistence.memory;

import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;

import java.util.Set;

// Todo remove it
public interface Db {

    void addUser(User user);

    User getUser(String login);

    User removeUser(String login);

    Set<User> getUsers();

    boolean containsLogin(String login);

    Set<Task> getTasks(String login);

    Task createTask(String login, String todo);

    Task getTask(String login, Integer id);

    Task getTask(Integer id);

    Task updateTask(String login, Task task);

    Task removeTask(String login, Integer id);

    Task removeTask(Integer id);

}
