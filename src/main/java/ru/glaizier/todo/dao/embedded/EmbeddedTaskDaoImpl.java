package ru.glaizier.todo.dao.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.Db;
import ru.glaizier.todo.domain.Task;

import java.util.List;

@Repository("mtd")
@Profile("memory")
public class EmbeddedTaskDaoImpl implements OverrideEmbeddedTaskDao {

    private final Db db;

    @Autowired
    public EmbeddedTaskDaoImpl(Db db) {
        this.db = db;
    }

    @Override
    public List<Task> findTasksByLogin(String login) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAA");
        return null;
    }

    @Override
    public Task findTaskByIdAndLogin(int id, String login) {
        System.out.println("BBBBBBBBBBBBBBBBBBBBBB");
        return null;
    }
}

/*

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

 */