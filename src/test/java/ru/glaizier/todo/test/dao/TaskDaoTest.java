package ru.glaizier.todo.test.dao;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.dao.Dao;
import ru.glaizier.todo.dao.task.TaskDao;
import ru.glaizier.todo.dao.user.UserDao;
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.domain.User;

import java.util.List;

import javax.transaction.Transactional;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
@ActiveProfiles(profiles = "memory")
public class TaskDaoTest {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private Dao dao;

    /*
    @Test
    public void get2TasksOnGetTasks() {
        assertThat(taskDao.findTasksByLogin("u").size(), is(2));
    }

    @Test
    public void getNullOnGetTasksForUnknownUser() {
        assertNull(taskDao.findTasksByLogin("dummyLogin"));
    }

    // Todo
   @Test
    public void getTheSameTaskAsCreatedOnCreateTask() {
        assertThat(taskDao.save(new Task("u", "created todo100")).getTodo(), is("created todo100"));
        assertThat(taskDao.findTaskByIdAndLogin(4, "u").getTodo(), is("created todo100"));
    }

    // Todo think about it after join user table will be done
//    @Test
//    public void getNullOnCreateTaskForUnknownUser() {
//        assertNull(taskDao.save(new Task("dummyLogin", "created todo100")));
//    }

    @Test
    public void getTaskOnGetTask() {
        assertThat(taskDao.findTaskByIdAndLogin(1, "u"),
                is(Task.builder().id(1).login("u").todo("todo1").build()));
    }

    @Test
    public void getNullOnGetTaskForUnknownId() {
        assertNull(taskDao.findTaskByIdAndLogin(3, "u"));
    }

    @Test
    public void getNullOnGetTaskForUnknownUser() {
        assertNull(taskDao.findTaskByIdAndLogin(1, "dummyUser"));
    }

    @Test
    public void getTaskOnGetTaskById() {
        assertThat(taskDao.findTaskById(1),
                is(Task.builder().id(1).login("u").todo("todo1").build()));
        assertThat(taskDao.findTaskById(3),
                is(Task.builder().id(3).login("a").todo("todo1").build()));
    }

    @Test
    public void getPreviousTaskOnUpdateTask() {
        assertThat(taskDao.save(Task.builder().id(1).login("u").todo("updated todo100").build()),
                is(Task.builder().id(1).login("u").todo("updated todo100").build()));
    }

    // Todo think about it after join user table will be done
    @Test
    public void getNullOnUpdateTaskForUnknownUser() {
        assertNull(taskDao.save("dummyLogin", Task.builder().id(1).login("u").todo("created todo100").build()));
    }

    @Test
    public void getRemovedTaskOnRemoveTask() {
        taskDao.delete(1);
        assertNull(taskDao.findTaskById(1));
    }
*/

    @Test
    @Transactional
    public void join() {
        Task task = taskDao.findTaskById(1);
        System.out.println(task.getUser());
    }

    @Test
//    @Transactional
    public void find() {
        User u = userDao.findUserByLogin("u");
        List<Task> tasksByUser = taskDao.findTasksByUser(u);
        System.out.println(tasksByUser.get(0).getTodo());
//        tasksByUser.forEach(System.out::println);

    }

    @Test
//    @Transactional
    public void findWithCheck() {
        Task u = dao.findTaskUserJoinedWithLoginCheck(1, "u");
//        System.out.println(u.getUser());
        System.out.println(u);
    }

    @Test
    @Transactional
    public void findJoined() {
        Task u = dao.findTaskUserJoined(1);
//        System.out.println(u.getUser());
        System.out.println(u);
    }

    @Test
//    @Transactional
    public void findJoinedDto() {
        System.out.println(dao.testDto(1));
    }
}