package ru.glaizier.dao;

import ru.glaizier.domain.Task;

import java.util.List;

// Todo refactor to handle tasks for specific user and think about design of Db
// Todo update javadoc to handle situations with null
public interface TaskDao {

    // collection

    /**
     * @return List of Tasks
     */
    List<Task> getTasks();

    /**
     * @param todo value for the task to create
     * @return created Task
     */
    Task createTask(String todo);

    // specific task

    /**
     * @param id of the task
     * @return Task for that id
     */
    Task getTask(int id);

    /**
     * @param id   of the task to update
     * @param todo value for the task to update
     * @return updated Task
     */
    Task updateTask(int id, String todo);

    /**
     * @param id of task to delete
     * @return deleted Task
     */
    Task removeTask(int id);

}
