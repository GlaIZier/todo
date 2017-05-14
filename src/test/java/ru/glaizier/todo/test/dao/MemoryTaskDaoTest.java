package ru.glaizier.todo.test.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.dao.TaskDao;
import ru.glaizier.todo.properties.PropertiesService;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class MemoryTaskDaoTest {

    @Autowired
    private PropertiesService propertiesService;

    @Autowired
    private TaskDao memoryTaskDao;

    @Test
    public void get2TasksOnGetTasks() {
        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
    }

    @Test
    public void getNullOnGetTasksForUnknownUser() {
        assertNull(memoryTaskDao.getTasks("dummyLogin"));
    }

//    @Test
//    public void get2TasksOnGetTasks() {
//        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
//    }
//
//    @Test
//    public void get2TasksOnGetTasks() {
//        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
//    }
//
//    @Test
//    public void get2TasksOnGetTasks() {
//        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
//    }
//
//    @Test
//    public void get2TasksOnGetTasks() {
//        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
//    }
//
//    @Test
//    public void get2TasksOnGetTasks() {
//        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
//    }


}

//    /**
//     * @param login of user to create Task for
//     * @param todo to create Task with
//     * @return created Task or null if there is no user with such login
//     */
//    Task createTask(String login, String todo);
//
//    // specific task
//
//    /**
//     * @param login of user to get Task for
//     * @param id    of the task
//     * @return Task for that id or null if there is no task or no such login
//     */
//    Task getTask(String login, int id);
//
//    /**
//     * @param login of user to update Task for
//     * @param task  to update
//     * @return previous Task or null if there is no task with this id or no such login
//     */
//    Task updateTask(String login, Task task);
//
//    /**
//     * @param login of user to remove Task for
//     * @param id    task id to delete
//     * @return deleted Task or null if there is no task with this id or no such login
//     */
//    Task removeTask(String login, int id);
//
//    /**
//     * Use this method to understand what means null in other methods return (no login or another)
//     *
//     * @param login of user
//     * @return true if there is such login or false otherwise
//     */
//    boolean containsLogin(String login);