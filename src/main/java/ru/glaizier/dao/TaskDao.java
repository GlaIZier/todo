package ru.glaizier.dao;

import ru.glaizier.domain.Task;

import java.util.List;

// Todo add spring JPA interface
// Todo move to repository pattern instead of Dao?
public interface TaskDao {

    // collection

    /**
     * @param login of user to get Tasks for
     * @return List of Tasks or null if there is no tasks for this login or no such login
     */
    List<Task> getTasks(String login);

    /**
     * @param login of user to create Task for
     * @param todo value for the task to create
     * @return created Task or null if there is no user with such login
     */
    Task createTask(String login, String todo);

    // specific task

    /**
     * @param login of user to get Task for
     * @param id of the task
     * @return Task for that id or null if there is no task or no such login
     */
    Task getTask(String login, int id);

    /**
     * @param login of user to update Task for
     * @param id   of the task to update
     * @param todo value for the task to update
     * @return previous Task or null if there is no task with this id or no such login
     */
    Task updateTask(String login, int id, String todo);

    /**
     * @param login of user to remove Task for
     * @param id of task to delete
     * @return deleted Task or null if there is no task with this id or no such login
     */
    Task removeTask(String login, int id);

    /**
     * Use this method to understand what means null in other methods return (no login or another)
     *
     * @param login of user
     * @return true if there is such login or false otherwise
     */
    boolean containsLogin(String login);

}
