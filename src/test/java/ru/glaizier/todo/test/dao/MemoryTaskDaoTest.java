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
import ru.glaizier.todo.dao.embedded.EmbeddedTaskDao;
import ru.glaizier.todo.dao.embedded.EmbeddedTaskDaoSql;
import ru.glaizier.todo.domain.Task;

import java.sql.SQLException;

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
    private TaskDao memoryTaskDao;

    @Autowired
    private EmbeddedTaskDaoSql embeddedTaskDaoSql;

    @Autowired
    private EmbeddedTaskDao embeddedTaskDao;

    @Test
    public void get2TasksOnGetTasks() {
        assertThat(memoryTaskDao.getTasks("u").size(), is(2));
    }

    @Test
    public void getNullOnGetTasksForUnknownUser() {
        assertNull(memoryTaskDao.getTasks("dummyLogin"));
    }

    @Test
    public void getTheSameTaskAsCreatedOnCreateTask() {
        assertThat(memoryTaskDao.createTask("u", "created todo100").getTodo(), is("created todo100"));
        assertThat(memoryTaskDao.getTask("u", 4).getTodo(), is("created todo100"));
    }

    @Test
    public void getNullOnCreateTaskForUnknownUser() {
        assertNull(memoryTaskDao.createTask("dummyLogin", "created todo100"));
    }

    @Test
    public void getTaskOnGetTask() {
        assertThat(memoryTaskDao.getTask("u", 1),
                is(Task.builder().id(1).login("u").todo("todo1").build()));
    }

    @Test
    public void getNullOnGetTaskForUnknownId() {
        assertNull(memoryTaskDao.getTask("u", 3));
    }

    @Test
    public void getNullOnGetTaskForUnknownUser() {
        assertNull(memoryTaskDao.getTask("dummyLogin", 1));
    }

    @Test
    public void getPreviousTaskOnUpdateTask() {
        assertThat(memoryTaskDao.updateTask("u", Task.builder().id(1).login("u").todo("updated todo100").build()),
                is(Task.builder().id(1).login("u").todo("todo1").build()));
    }

    @Test
    public void getNullOnUpdateTaskForUnknownId() {
        assertNull(memoryTaskDao.updateTask("u", Task.builder().id(3).login("u").todo("created todo100").build()));
    }

    @Test
    public void getNullOnUpdateTaskForUnknownUser() {
        assertNull(memoryTaskDao.updateTask("dummyLogin", Task.builder().id(1).login("u").todo("created todo100").build()));
    }

    @Test
    public void getRemovedTaskOnRemoveTask() {
        assertThat(memoryTaskDao.removeTask("u", 1),
                is(Task.builder().id(1).login("u").todo("todo1").build()));
    }

    @Test
    public void getNullOnRemoveTaskForUnknownId() {
        assertNull(memoryTaskDao.removeTask("u", 3));
    }

    @Test
    public void getNullOnRemoveTaskForUnknownUser() {
        assertNull(memoryTaskDao.removeTask("dummyLogin", 1));
    }

    @Test
    public void getTrueOnContainsUser() {
        assertThat(memoryTaskDao.containsLogin("u"), is(true));
    }

    @Test
    public void getFalseOnContainsUserUnknownUser() {
        assertThat(memoryTaskDao.containsLogin("dummyLogin"), is(false));
    }

    @Test
    public void testEmbeddedTaskDaoSql() throws SQLException {
        embeddedTaskDaoSql.test();
    }

    @Test
    public void testEmbeddedTaskDao() throws SQLException {
        Task taskById = embeddedTaskDao.findTaskById(1);
        System.out.println(taskById);
    }

    @Test
    public void testFindTaskByIdAndLogin() throws SQLException {
        Task taskById = embeddedTaskDao.findTaskByIdAndLogin(2, "a");
        System.out.println(taskById);
    }

}