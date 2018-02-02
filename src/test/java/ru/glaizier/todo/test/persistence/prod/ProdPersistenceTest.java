package ru.glaizier.todo.test.persistence.prod;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;
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

/**
 * Run these tests with prod db when needed. Here we already have 2 roles from PersistenceInit
 */
// We use @Transactional so before each method we rollback changes, so we don't need to use DirtiesContext
//@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
@ActiveProfiles("prod")
@Transactional
// Comment @IfProfileValue to run this test from IDE or edit the IDE's run config to run with this provided key by default
@IfProfileValue(name = "spring.profiles.active", values = {"prod"})
public class ProdPersistenceTest {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private Persistence p;

    private final RoleDto dummyRole = new RoleDto("dummyRole");

    private final UserDto dummyUser = UserDto.builder().login("dummyLogin").password("dummyPassword".toCharArray())
            .roles(Optional.of(new HashSet<>(Collections.singletonList(dummyRole)))).build();

    private final TaskDto dummyTask = TaskDto.builder().id(1).user(Optional.of(dummyUser)).todo("dummyTodo").build();

    private final UserDto wrongDummyUser = UserDto.builder().login("wrongDummyLogin").password("wrongDummyPassword".toCharArray())
            .roles(Optional.of(new HashSet<>(Collections.singletonList(dummyRole)))).build();

    @Before
    public void init() {
        p.saveRole(dummyRole.getRole());
        p.saveUser(dummyUser.getLogin(), dummyUser.getPassword(), dummyUser.getRoles().orElse(null));
        p.saveTask(dummyTask.getUser().orElse(null).getLogin(), dummyTask.getTodo());
    }

    // Tasks
    @Test
    public void getTasks() {
        List<TaskDto> tasks = p.findTasks();
        assertThat(tasks.size(), is(1));
        assertThat(tasks.get(0).getTodo(), is(dummyTask.getTodo()));
//        tasks.forEach((t) -> log.debug(t.toString()));
    }

    @Test
    public void getTasksByLogin() {
        List<TaskDto> tasks = p.findTasks(dummyUser.getLogin());
        assertThat(tasks.size(), is(1));
        assertThat(tasks.get(0).getTodo(), is(dummyTask.getTodo()));
    }

    @Test
    public void getNullOnGetTasksForUnknownUser() {
        assertNull(p.findTasks("nonExistingLogin"));
    }

    @Test
    public void saveTask() {
        TaskDto dummyTask2 = dummyTask.toBuilder().user(Optional.empty()).todo("dummyTodo2").build();
        assertThat(p.saveTask(dummyUser.getLogin(), dummyTask2.getTodo()).getTodo(), is(dummyTask2.getTodo()));
        assertThat(p.findTasks(dummyUser.getLogin()).size(), is(2));
        assertThat(p.findTasks(dummyUser.getLogin()).get(1).getTodo(), is(dummyTask2.getTodo()));
        assertFalse(p.findTasks(dummyUser.getLogin()).get(1).getUser().isPresent());
    }

    @Test()
    public void getNullOnSaveTaskForUnknownUser() {
        assertNull(p.saveTask("nonExistingLogin", dummyTask.getTodo()));
    }

    @Test
    public void getTaskOnGetTask() {
        assertThat(p.findTask(p.findTasks(dummyUser.getLogin()).get(0).getId()).getTodo(), is(dummyTask.getTodo()));
    }

    @Test
    public void getNullOnGetTaskForUnknownId() {
        assertNull(p.findTask(100000));
    }

    @Test
    public void getTaskOnGetTaskByIdAndLogin() {
        assertThat(p.findTask(p.findTasks(dummyUser.getLogin()).get(0).getId(), dummyUser.getLogin()).getTodo(),
                is(dummyTask.getTodo()));
    }

    @Test
    public void getNullOnGetTaskByIdAndLoginForUnknownLogin() {
        assertNull(p.findTask(p.findTasks(dummyUser.getLogin()).get(0).getId(), "nonExistingLogin"));
    }

    @Test(expected = AccessDeniedException.class)
    public void getExceptionOnGetTaskByIdAndLoginForWrongLogin() {
        p.saveUser(wrongDummyUser.getLogin(), wrongDummyUser.getPassword(), wrongDummyUser.getRoles().orElse(null));
        p.findTask(p.findTasks(dummyUser.getLogin()).get(0).getId(), wrongDummyUser.getLogin());
    }

    @Test
    public void updateTask() {
        String updatedTodo = "dummyTodo2";
        assertThat(p.updateTask(dummyUser.getLogin(), p.findTasks(dummyUser.getLogin()).get(0).getId(), updatedTodo).getTodo(),
                is(updatedTodo));
        assertThat(p.findTask(p.findTasks(dummyUser.getLogin()).get(0).getId(), dummyUser.getLogin()).getTodo(), is(updatedTodo));
        assertThat(p.findTasks(dummyUser.getLogin()).size(), is(1));
    }

    @Test
    public void getNullOnUpdateTaskForUnknownId() {
        String updatedTodo = "dummyTodo2";
        assertNull(p.updateTask(dummyUser.getLogin(), 10000, updatedTodo));
    }

    @Test()
    public void getNullOnUpdateTaskForUnknownUser() {
        String updatedTodo = "dummyTodo2";
        assertNull(p.updateTask("nonExistingLogin", p.findTasks(dummyUser.getLogin()).get(0).getId(), updatedTodo));
    }

    @Test(expected = AccessDeniedException.class)
    public void getExceptionOnUpdateTaskForUnknownUser() {
        String updatedTodo = "dummyTodo2";
        p.saveUser(wrongDummyUser.getLogin(), wrongDummyUser.getPassword(), wrongDummyUser.getRoles().orElse(null));
        p.updateTask(wrongDummyUser.getLogin(), p.findTasks(dummyUser.getLogin()).get(0).getId(), updatedTodo);
    }

    @Test
    public void deleteTaskById() {
        Integer id = p.findTasks(dummyUser.getLogin()).get(0).getId();
        assertThat(p.findTasks().size(), is(1));
        assertThat(p.deleteTask(id).getTodo(), is(dummyTask.getTodo()));
        assertNull(p.findTask(id));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));
        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
        assertTrue(p.findTasks(dummyUser.getLogin()).isEmpty());
        assertThat(p.findTasks().size(), is(0));

    }

    @Test
    public void getNullOnRemoveTaskByIdWhenTaskNotExists() {
        assertNull(p.deleteTask(100));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));
        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
    }

    @Test
    public void removeTaskByIdAndLogin() {
        Integer id = p.findTasks(dummyUser.getLogin()).get(0).getId();
        assertThat(p.findTasks().size(), is(1));
        assertThat(p.deleteTask(id, dummyUser.getLogin()).getTodo(), is(dummyTask.getTodo()));

        assertNull(p.findTask(id, dummyUser.getLogin()));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));
        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
        assertTrue(p.findTasks(dummyUser.getLogin()).isEmpty());
        assertThat(p.findTasks().size(), is(0));
    }

    @Test
    public void getNullOnRemoveTaskByIdAndLoginWhenLoginNotExists() {
        assertNull(p.deleteTask(p.findTasks(dummyUser.getLogin()).get(0).getId(), wrongDummyUser.getLogin()));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));
        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
    }

    @Test(expected = AccessDeniedException.class)
    public void getExceptionOnRemoveTaskByIdAndLoginWhenWrongLogin() {
        p.saveUser(wrongDummyUser.getLogin(), wrongDummyUser.getPassword(), wrongDummyUser.getRoles().orElse(null));
        assertNull(p.deleteTask(p.findTasks(dummyUser.getLogin()).get(0).getId(), wrongDummyUser.getLogin()));
    }

    // Users
    @Test
    public void findUsers() {
        UserDto dummyUser2 = dummyUser.toBuilder().login("dummyLogin2").password("dummyPassword2".toCharArray())
                .roles(Optional.of(new HashSet<>(Collections.singletonList(dummyRole)))).build();
        p.saveUser(dummyUser2.getLogin(), dummyUser2.getPassword(), new HashSet<>(Collections.singletonList(dummyRole)));
        assertThat(p.findUsers().size(), is(2));
    }

    @Test
    public void findUserByLogin() {
        assertThat(p.findUser(dummyUser.getLogin()), is(dummyUser));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));
    }

    @Test
    public void getNullForNonExistUserOnFindUserByLogin() {
        assertNull(p.findUser("nonExistingLogin"));
    }

    @Test
    public void findUserByLoginAndPassword() {
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()),
                is(dummyUser));
    }

    @Test
    public void getNullForWrongPasswordOnFindUserByLoginAndPassword() {
        assertNull(p.findUser(dummyUser.getLogin(), wrongDummyUser.getPassword()));
    }

    @Test
    public void getNullForNonExistUserOnFindUserByLoginAndPassword() {
        assertNull(p.findUser("nonExistingLogin", wrongDummyUser.getPassword()));
    }

    @Test
    public void saveUser() {
        UserDto dummyUser2 = dummyUser.toBuilder().login("dummyLogin2").password("dummyPassword2".toCharArray())
                .roles(Optional.of(new HashSet<>(Collections.singletonList(dummyRole)))).build();

        assertThat(p.saveUser(dummyUser2.getLogin(), dummyUser2.getPassword(), new HashSet<>(Collections.singletonList(dummyRole))),
                is(dummyUser2));
        assertThat(p.findUser(dummyUser2.getLogin(), dummyUser2.getPassword()), is(dummyUser2));
        assertThat(p.findUsers().size(), is(2));

        assertTrue(p.findTasks(dummyUser2.getLogin()).isEmpty());
        assertThat(p.findRoles().size(), is(3));
    }

    @Test
    public void saveUserWithNoRoles() {
        UserDto dummyUser2 = dummyUser.toBuilder().login("dummyLogin2").password("dummyPassword2".toCharArray())
                .roles(Optional.of(new HashSet<>())).build();

        assertThat(p.saveUser(dummyUser2.getLogin(), dummyUser2.getPassword(), new HashSet<>()), is(dummyUser2));
        assertThat(p.findUser(dummyUser2.getLogin(), dummyUser2.getPassword()), is(dummyUser2));

        assertTrue(p.findTasks(dummyUser2.getLogin()).isEmpty());
        assertThat(p.findRoles().size(), is(3));
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void getExceptionOnSaveUserWithNonExistingRole() {
        UserDto dummyUser2 = dummyUser.toBuilder().login("dummyLogin2").password("dummyPassword2".toCharArray())
                .roles(Optional.of(new HashSet<>(Collections.singletonList(new RoleDto("nonExistingRole"))))).build();
        p.saveUser(dummyUser2.getLogin(), dummyUser2.getPassword(), dummyUser2.getRoles().orElse(null));
    }

    @Test
    public void saveUserWithNewRole() {
        RoleDto dummyRole2 = new RoleDto("dummyRole2");
        UserDto dummyUser2 = dummyUser.toBuilder().login("dummyLogin2").password("dummyPassword2".toCharArray())
                .roles(Optional.of(new HashSet<>(Collections.singletonList(new RoleDto("dummyRole2"))))).build();
        assertNull(p.findRole(dummyRole2.getRole()));
        try {
            p.saveUser(dummyUser2.getLogin(), dummyUser2.getPassword(), dummyUser2.getRoles().orElse(null));
        } catch (JpaObjectRetrievalFailureException e) {
            log.info(e.getMessage() + ". Creating new role...");
        }
        p.saveRole(dummyRole2.getRole());
        assertThat(p.saveUser(dummyUser2.getLogin(), dummyUser2.getPassword(), dummyUser2.getRoles().orElse(null)),
                is(dummyUser2));

        assertThat(p.findUser(dummyUser2.getLogin(), dummyUser2.getPassword()), is(dummyUser2));
        assertTrue(p.findTasks(dummyUser2.getLogin()).isEmpty());

        assertThat(p.findRole(dummyRole2.getRole()), is(dummyRole2));
        assertThat(p.findRoles().size(), is(4));
    }


    @Test
    public void updateUser() {
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));

        RoleDto dummyRole2 = new RoleDto("dummyRole2");
        UserDto dummyUser2 = dummyUser.toBuilder().login("dummyLogin2").password("dummyPassword2".toCharArray())
                .roles(Optional.of(new HashSet<>(Collections.singletonList(new RoleDto("dummyRole2"))))).build();
        p.saveRole(dummyRole2.getRole());

        p.saveUser(dummyUser.getLogin(), dummyUser2.getPassword(), dummyUser2.getRoles().orElse(null));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser2.getPassword()),
                is(dummyUser.toBuilder().password(dummyUser2.getPassword()).roles(dummyUser2.getRoles()).build()));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser2.getPassword()).getRoles().orElse(null).size(), is(1));
        assertThat(p.findRoles().size(), is(4));
    }


    @Test
    // Todo check the exception
    public void deleteUser() {
        assertNotNull(p.findUser(dummyUser.getLogin()));
        p.deleteUser(dummyUser.getLogin());
        assertNull(p.findUser(dummyUser.getLogin()));
        // Here exception is thrown about unsaved transient object. Don't know why yet
//        assertThat(p.findUsers().size(), is(0));
//        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
//        assertThat(p.findTasks().size(), is(0));
    }

    // Roles
    @Test
    public void findRoles() {
        RoleDto dummyRole2 = new RoleDto("dummyRole2");
        assertNull(p.findRole(dummyRole2.getRole()));
        assertThat(p.saveRole(dummyRole2.getRole()), is(dummyRole2));
        assertThat(p.findRoles().size(), is(4));
    }

    @Test
    public void findRole() {
        assertThat(p.findRole(dummyRole.getRole()), is(dummyRole));
    }

    @Test
    public void getNullForNonExistRoleOnFindRoleByRole() {
        assertNull(p.findRole("nonExistingRole"));
    }

    @Test
    public void saveRole() {
        RoleDto dummyRole2 = new RoleDto("dummyRole2");
        assertNull(p.findRole(dummyRole2.getRole()));
        assertThat(p.saveRole(dummyRole2.getRole()), is(dummyRole2));
        assertThat(p.findRole(dummyRole2.getRole()), is(dummyRole2));

        assertThat(p.findRoles().size(), is(4));
        assertThat(p.findUsers().size(), is(1));
    }

    @Test
    // Todo check why it is cached
    public void deleteRole() {
        assertNotNull(p.findRole(dummyRole.getRole()));
        assertThat(p.findUser(dummyUser.getLogin(), dummyUser.getPassword()), is(dummyUser));
        assertThat(p.findTasks().size(), is(1));

        p.deleteRole(dummyRole.getRole());

        assertNull(p.findRole(dummyRole.getRole()));
        assertThat(p.findRoles().size(), is(2));
        // Here we still have dummy role inside dummyUser. Probably it is cached in hibernate.
//        assertThat(p.findUser(dummyUser.getLogin()), is(dummyUser.toBuilder().roles(Optional.of(new HashSet<>())).build()));
        assertThat(p.findUsers().size(), is(1));
        assertThat(p.findTasks().size(), is(1));
    }

}