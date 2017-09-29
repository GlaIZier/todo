package ru.glaizier.todo.persistence.memory.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.persistence.Persistence;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Repository
@Profile("memory")
@Qualifier("taskDao")
public class TaskDaoImpl implements OverrideTaskDao {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Persistence persistence;

    @Autowired
    public TaskDaoImpl(Persistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public List<Task> findAll() {
        log.info("findAll() memory implementation");
        return null;
    }

    @Override
    public List<Task> findTasksByUser(User user) {
        return null;
    }

    @Override
    public Task findTaskById(Integer id) {
        return null;
    }

    @Override
    public Task findTaskByIdAndUser(Integer id, User user) {
        return null;
    }

    @Override
    public <S extends Task> S save(S s) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    //
//    @Override
//    public List<Task> findTasksByLogin(String login) {
//        log.info("findTasksByLogin memory implementation...");
//        Set<Task> tasks = db.getTasks(login);
//        if (tasks == null)
//            return null;
//        return tasks.stream().sorted(Comparator.comparingInt(Task::getId))
//                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
//    }
//
//    @Override
//    public Task findTaskByIdAndLogin(Integer id, String login) {
//        log.info("findTaskByIdAndLogin memory implementation...");
//        return db.getTask(login, id);
//    }
//
//    @Override
//    public Task findTaskById(Integer id) {
//        log.info("findTaskById memory implementation...");
//        return db.getTask(id);
//    }
//
//    @Override
//    public <S extends Task> S save(S s) {
////        if (s.getId() == null || db.getTask(s.getLogin(), s.getId()) == null) {
////            log.info("save (createTask) memory implementation...");
////            Task createdTask = db.createTask(s.getLogin(), s.getTodo());
////            s.setId(createdTask.getId());
////        } else {
////            log.info("save (updateTask) memory implementation...");
////            db.updateTask(s.getLogin(), s);
////        }
//        return s;
//    }
//
//    @Override
//    public void delete(Integer integer) {
//        log.info("delete memory implementation...");
//        db.removeTask(integer);
//    }
}