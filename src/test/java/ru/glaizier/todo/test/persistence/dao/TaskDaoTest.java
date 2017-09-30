package ru.glaizier.todo.test.persistence.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.persistence.dao.RoleDao;
import ru.glaizier.todo.persistence.dao.TaskDao;
import ru.glaizier.todo.persistence.dao.UserDao;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
@Transactional
public class TaskDaoTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    private final Role dummyRole = new Role("dummyRole");

    private final User dummyUser = User.builder().login("dummyLogin").password("dummyPassword".toCharArray())
            .roles(new HashSet<>(Collections.singletonList(dummyRole))).build();

    private final Task dummyTask = Task.builder().user(dummyUser).todo("dummyTodo").build();

    @Before
    public void init() {
        roleDao.save(dummyRole);
        userDao.save(dummyUser);
        taskDao.save(dummyTask);
    }


    @Test
    public void findAll() {
        assertThat(taskDao.findAll().size(), is(4));
    }

    @Test
    public void findByUser() {
        List<Task> tasks = taskDao.findTasksByUser(dummyUser);
        assertThat(tasks.size(), is(1));
        assertThat(tasks.get(0), is(dummyTask));
    }

    @Test
    public void getEmptyListOnGetTasksForUnknownUser() {
        assertTrue(taskDao.findTasksByUser(User.builder().login("nonExistingLogin").password("".toCharArray()).build())
                .isEmpty());
    }

    @Test
    public void getTheSameTaskAsCreatedOnCreateTask() {
        Task dummyTask2 = Task.builder().user(dummyUser).todo("dummyTodo2").build();
        assertThat(taskDao.save(dummyTask2),
                is(dummyTask2));
        assertThat(taskDao.findTasksByUser(dummyUser).get(1), is(dummyTask2));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void getExceptionOnCreateTaskForUnknownUser() {
        User nonExistingUser = User.builder().login("nonExistingLogin").password("".toCharArray()).build();
        Task nonExistingTask = Task.builder().user(nonExistingUser).todo("nonExistingTodo").build();
        taskDao.save(nonExistingTask);
    }

    @Test
    public void getTaskOnGetTask() {
        assertThat(taskDao.findTaskById(4), is(dummyTask));
    }

    @Test
    public void getNullOnGetTaskForUnknownId() {
        assertNull(taskDao.findTaskById(100));
    }

    @Test
    public void getTaskOnGetTaskByIdAndUser() {
        assertThat(taskDao.findTaskByIdAndUser(4, dummyUser),
                is(dummyTask));
    }

    @Test
    public void getNullOnGetTaskForUnknownUser() {
        User nonExistingUser = User.builder().login("nonExistingLogin").password("".toCharArray()).build();
        assertNull(taskDao.findTaskByIdAndUser(4, nonExistingUser));
    }

    @Test
    public void updateTask() {
        Task updatedDummyTask = dummyTask.toBuilder().todo("updatedDummyTodo").build();
//        Task updatedDummyTask = taskDao.findTaskById(4).toBuilder().todo("updatedDummyTodo").build();
        assertThat(taskDao.save(updatedDummyTask),
                is(updatedDummyTask));
        assertThat(taskDao.findTaskById(4), is(updatedDummyTask));
    }

    @Test(expected = Exception.class)
    public void getExceptionOnUpdateTaskForUnknownUser() {
        User nonExistingUser = User.builder().login("nonExistingLogin").password("".toCharArray()).build();
        Task updatedDummyTask = taskDao.findTaskById(4).toBuilder().user(nonExistingUser).todo("updatedDummyTodo").build();
        log.error(taskDao.save(updatedDummyTask).toString());
    }

    @Test
    public void removeTask() {
        assertNotNull(taskDao.findTaskById(4));
        taskDao.delete(4);
        assertNull(taskDao.findTaskById(4));
        assertThat(userDao.findUserByLogin("dummyLogin"), is(dummyUser));
        assertThat(roleDao.findRoleByRole("dummyRole"), is(dummyRole));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getExceptionWhenRemovingNonExistingTaskOnRemoveTask() {
        taskDao.delete(100);
    }

}