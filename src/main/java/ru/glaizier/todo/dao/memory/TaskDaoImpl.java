package ru.glaizier.todo.dao.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.OverrideTaskDao;
import ru.glaizier.todo.domain.Task;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Repository
@Profile("memory")
public class TaskDaoImpl implements OverrideTaskDao {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Db db;

    @Autowired
    public TaskDaoImpl(Db db) {
        this.db = db;
    }

    @Override
    public List<Task> findTasksByLogin(String login) {
        log.debug("findTasksByLogin memory implementation...");
        Set<Task> tasks = db.getTasks(login);
        if (tasks == null)
            return null;
        return tasks.stream().sorted(Comparator.comparingInt(Task::getId))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public Task findTaskByIdAndLogin(Integer id, String login) {
        log.debug("findTaskByIdAndLogin memory implementation...");
        return db.getTask(login, id);
    }

    @Override
    public Task findTaskById(Integer id) {
        log.debug("findTaskById memory implementation...");
        return db.getTask(id);
    }

    @Override
    public <S extends Task> S save(S s) {
        if (s.getId() == null || db.getTask(s.getLogin(), s.getId()) == null) {
            log.debug("save (createTask) memory implementation...");
            db.createTask(s.getLogin(), s.getTodo());
        } else {
            log.debug("save (updateTask) memory implementation...");
            db.updateTask(s.getLogin(), s);
        }
        return s;
    }

    @Override
    public void delete(Integer integer) {
        log.debug("findTasksByLogin memory implementation...");
        db.removeTask(integer);
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