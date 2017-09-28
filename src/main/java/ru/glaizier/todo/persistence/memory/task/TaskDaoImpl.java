package ru.glaizier.todo.persistence.memory.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

//@Repository
//@Profile("memory")
public class TaskDaoImpl implements OverrideTaskDao {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//
//    private final Db db;
//
//    @Autowired
//    public TaskDaoImpl(Db db) {
//        this.db = db;
//    }
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