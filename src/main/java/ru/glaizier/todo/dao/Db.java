package ru.glaizier.todo.dao;

import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.User;

import java.util.Set;

public interface Db {

    void addUser(User user);

    User getUser(String login);

    User removeUser(String login);

    Set<User> getUsers();

    boolean containsLogin(String login);

    Set<Task> getTasks(String login);

    Task createTask(String login, String todo);

    Task getTask(String login, int id);

    Task updateTask(String login, Task task);

    Task removeTask(String login, int id);

}
