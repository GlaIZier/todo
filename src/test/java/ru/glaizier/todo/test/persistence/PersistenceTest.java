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
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

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

    // Todo update persistence to handle updateUser, updateRole
    private final TaskDto dummyTask = TaskDto.builder().id(4).user(Optional.of(dummyUser)).todo("dummyTodo").build();

    private final UserDto wrongDummyUser = UserDto.builder().login("wrongDummyLogin").password("wrongDummyPassword".toCharArray())
            .roles(Optional.of(new HashSet<>(Collections.singletonList(dummyRole)))).build();

    @Before
    public void init() {
        p.saveRole(dummyRole.getRole());
        p.saveUser(dummyUser.getLogin(), dummyUser.getPassword(), dummyUser.getRoles().orElse(null));
        p.saveTask(dummyTask.getUser().orElse(null).getLogin(), dummyTask.getTodo());
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

    @Test
    public void saveTask() {
        TaskDto dummyTask2 = dummyTask.toBuilder().id(5).user(Optional.empty()).todo("dummyTodo2").build();
        assertThat(p.saveTask(dummyUser.getLogin(), dummyTask2.getTodo()), is(dummyTask2));
        assertThat(p.findTasksByLogin(dummyUser.getLogin()).get(1), is(dummyTask2));
    }

    @Test()
    public void getNullOnSaveTaskForUnknownUser() {
        assertNull(p.saveTask("nonExistingLogin", dummyTask.getTodo()));
    }

    @Test
    public void getTaskOnGetTask() {
        assertThat(p.findTask(dummyTask.getId()), is(dummyTask));
    }

    @Test
    public void getNullOnGetTaskForUnknownId() {
        assertNull(p.findTask(100));
    }

    @Test
    public void getTaskOnGetTaskByIdAndLogin() {
        assertThat(p.findTask(dummyTask.getId(), dummyUser.getLogin()),
                is(dummyTask.toBuilder().user(Optional.empty()).build()));
    }

    @Test
    public void getNullOnGetTaskByIdAndLoginForUnknownLogin() {
        assertNull(p.findTask(dummyTask.getId(), "nonExistingLogin"));
    }

    @Test(expected = AccessDeniedException.class)
    public void getExceptionOnGetTaskByIdAndLoginForWrongLogin() {
        p.saveUser(wrongDummyUser.getLogin(), wrongDummyUser.getPassword(), wrongDummyUser.getRoles().orElse(null));
        p.findTask(4, wrongDummyUser.getLogin());
    }

    @Test
    public void updateTask() {
        String updatedTodo = "dummyTodo2";
        TaskDto updatedTask = dummyTask.toBuilder().todo(updatedTodo).user(Optional.empty()).build();
        assertThat(p.updateTask(dummyUser.getLogin(), dummyTask.getId(), updatedTodo),
                is(updatedTask));
        assertThat(p.findTask(4, dummyUser.getLogin()), is(updatedTask));
    }

    @Test()
    public void getNullOnUpdateTaskForUnknownUser() {
        String updatedTodo = "dummyTodo2";
        assertNull(p.updateTask("nonExistingLogin", dummyTask.getId(), updatedTodo));
    }

    @Test(expected = AccessDeniedException.class)
    public void getExceptionOnUpdateTaskForUnknownUser() {
        String updatedTodo = "dummyTodo2";
        p.saveUser(wrongDummyUser.getLogin(), wrongDummyUser.getPassword(), wrongDummyUser.getRoles().orElse(null));
        p.updateTask(wrongDummyUser.getLogin(), dummyTask.getId(), updatedTodo);
    }

    @Test
    public void removeTaskById() {
        assertThat(p.deleteTask(4), is(dummyTask));
        assertNull(p.findTask(4));
    }

    @Test
    public void getNullOnRemoveTaskByIdWhenTaskNotExists() {
        assertNull(p.deleteTask(100));
    }

    @Test
    public void removeTaskByIdAndLogin() {
        assertThat(p.deleteTask(4, dummyUser.getLogin()), is(dummyTask));
        assertNull(p.findTask(4, dummyUser.getLogin()));
    }

    @Test
    public void getNullOnRemoveTaskByIdAndLoginWhenLoginNotExists() {
        assertNull(p.deleteTask(4, wrongDummyUser.getLogin()));
    }

    @Test(expected = AccessDeniedException.class)
    public void getExceptionOnRemoveTaskByIdAndLoginWhenWrongLogin() {
        p.saveUser(wrongDummyUser.getLogin(), wrongDummyUser.getPassword(), wrongDummyUser.getRoles().orElse(null));
        assertNull(p.deleteTask(4, wrongDummyUser.getLogin()));
    }

                    /*

    @Test
    public void removeTaskById() {
        assertThat(p.deleteTask(4), is (dummyTask));
        assertNull(p.findTaskById(4));
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
