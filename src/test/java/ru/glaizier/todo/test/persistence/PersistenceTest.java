package ru.glaizier.todo.test.persistence;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.Persistence;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class PersistenceTest {

    @Autowired
    private Persistence p;

    private final RoleDto dummyRole = new RoleDto("dummyRole");

    private final UserDto dummyUser = UserDto.builder().login("dummyLogin").password("dummyPassword".toCharArray())
            .roles(Optional.of(new HashSet<>(Collections.singletonList(dummyRole)))).build();

    // Todo update persistence to handle updateTask, updateUser, updateRole
    private final TaskDto dummyTask = TaskDto.builder().id(4).user(Optional.of(dummyUser)).todo("dummyTodo").build();

    @Before
    public void init() {
        p.saveRole(dummyRole.getRole());
        p.saveUser(dummyUser.getLogin(), dummyUser.getPassword(), dummyUser.getRoles().orElse(null));
        p.saveTask(dummyTask.getUser().orElse(null).getLogin(), dummyTask.getId(), dummyTask.getTodo());
    }

    @Test
    public void getTasks() {
        List<TaskDto> tasks = p.findTasksByLogin(dummyUser.getLogin());
        assertThat(tasks.size(), is(1));
        assertThat(tasks.get(0), is(dummyTask.toBuilder().user(Optional.empty()).build()));
    }

    @Test
    public void getNullOnGetTasksForUnknownUser() {
        assertNull(p.findTasksByLogin("nonExistingLogin"));
    }

    /*
    @Test
    public void getEmptyListOnGetTasksForUnknownUser() {
        assertTrue(p.findTasksByLogin("nonExistingLogin").isEmpty());
    }

    @Test
    public void getTheSameTaskAsCreatedOnCreateTask() {
        TaskDto dummyTask2 = dummyTask.toBuilder().id(101).todo("dummyTodo2").build();
        assertThat(p.saveTask(dummyUser.getLogin(), 101, dummyTask2.getTodo()), is(dummyTask2));
        assertThat(p.findTasksByLogin(dummyUser.getLogin()).get(1), is(dummyTask2));
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

    @Test
    public void deleteRole() {
        assertThat(p.findUserByLogin(dummyUser.getLogin()), is(dummyUser));
        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
        System.out.println(p.findUserByLogin(dummyUser.getLogin()));
        p.deleteRole(dummyRole.getRole());
        System.out.println(p.findUserByLogin(dummyUser.getLogin()));
        assertThat(p.findUserByLogin(dummyUser.getLogin()), is(dummyUser.toBuilder().roles(Optional.of(new HashSet<>())).build()));
        assertNull(p.findRole(dummyRole.getRole()));
    }

    @Test
    public void deleteRoleWithoutRoleExistenceCheck() {
        assertThat(p.findUserByLogin(dummyUser.getLogin()), is(dummyUser));
        System.out.println(p.findUserByLogin(dummyUser.getLogin()));
        p.deleteRole(dummyRole.getRole());
        System.out.println(p.findUserByLogin(dummyUser.getLogin()));
        assertThat(p.findUserByLogin(dummyUser.getLogin()), is(dummyUser.toBuilder().roles(Optional.of(new HashSet<>())).build()));

    }
*/
}
