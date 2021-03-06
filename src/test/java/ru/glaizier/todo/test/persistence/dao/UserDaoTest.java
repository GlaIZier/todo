package ru.glaizier.todo.test.persistence.dao;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.transaction.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.persistence.dao.RoleDao;
import ru.glaizier.todo.persistence.dao.UserDao;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
@Transactional
// Make these tests work only with default profile because there are no dao for memory implementation
// And the prod db doesn't contain any test data (like "u" and "a" users to rely on)
// Comment @IfProfileValue to run this test from IDE or edit the IDE's run config to run with this provided key by default
@IfProfileValue(name = "spring.profiles.active", values = {"default"})
public class UserDaoTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    private final Role dummyRole = new Role("dummyRole");

    private final User dummyUser = User.builder().login("dummyLogin").password("dummyPassword".toCharArray())
            .roles(new HashSet<>(Collections.singletonList(dummyRole))).build();

    private User u = User.builder().login("u").password("p".toCharArray())
            .roles(new HashSet<>(Collections.singletonList(Role.USER))).build();

    private User a = User.builder().login("a").password("p".toCharArray())
            .roles(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN))).build();

    @Before
    public void init() {
        roleDao.save(dummyRole);
        userDao.save(dummyUser);
    }

    @Test
    public void findUserByLogin() {
        assertThat(userDao.findUserByLogin(dummyUser.getLogin()), is(dummyUser));
    }

    @Test
    public void getNullForNonExistUserOnFindUserByLogin() {
        assertNull(userDao.findUserByLogin("nonExistingLogin"));
    }

    @Test
    public void findUserByLoginAndPassword() {
        assertThat(userDao.findUserByLoginAndPassword(dummyUser.getLogin(), dummyUser.getPassword()),
                is(dummyUser));
    }

    @Test
    public void getNullForWrongPasswordOnFindUserByLoginAndPassword() {
        assertNull(userDao.findUserByLoginAndPassword(dummyUser.getLogin(), "wrongPassword".toCharArray()));
    }

    @Test
    public void getNullForNonExistUserOnFindUserByLoginAndPassword() {
        assertNull(userDao.findUserByLoginAndPassword("nonExistingLogin", "dummyPassword".toCharArray()));
    }

    @Test
    public void findAll() {
        assertThat(userDao.findAll().size(), is(3));
    }

    @Test
    public void save() {
        assertThat(userDao.save(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(Role.USER))).build()),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(Role.USER))).build()));
        assertThat(userDao.findUserByLogin("savedUser"),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(Role.USER))).build()));
    }

    @Test
    public void saveWithNoRoles() {
        assertThat(userDao.save(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>()).build()),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>()).build()));
        assertThat(userDao.findUserByLogin("savedUser"),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>()).build()));
    }

    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void getExceptionOnSaveWithNonExistingRole() {
        assertThat(userDao.save(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(new Role("nonExistingRole")))).build()),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(new Role("nonExistingRole")))).build()));
    }

    @Test
    public void saveWithNewRole() {
        assertNull(roleDao.findRoleByRole("savedRole"));
        try {
            userDao.save(User.builder().login("savedUser").password("savedPassword".toCharArray())
                    .roles(new HashSet<>(Collections.singletonList(new Role("savedRole")))).build());
        } catch (JpaObjectRetrievalFailureException e) {
            log.info(e.getMessage() + ". Creating new role...");
        }
        roleDao.save(new Role("savedRole"));
        assertThat(userDao.save(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(new Role("savedRole")))).build()),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(new Role("savedRole")))).build()));
    }

    @Test
    public void update() {
        assertThat(userDao.save(User.builder().login(dummyUser.getLogin()).password("savedPassword".toCharArray())
                        .roles(dummyUser.getRoles()).build()),
                is(dummyUser.toBuilder().password("savedPassword".toCharArray()).build())
        );

        assertThat(userDao.findUserByLogin(dummyUser.getLogin()),
                is(dummyUser.toBuilder().password("savedPassword".toCharArray()).build()));
    }

    @Test
    public void delete() {
        assertNotNull(userDao.findUserByLogin(dummyUser.getLogin()));
        userDao.delete(dummyUser.getLogin());
        assertNull(userDao.findUserByLogin(dummyUser.getLogin()));
    }
}
