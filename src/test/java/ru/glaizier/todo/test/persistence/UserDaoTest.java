package ru.glaizier.todo.test.persistence;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.persistence.sql.PersistenceSql;
import ru.glaizier.todo.persistence.user.UserDao;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import javax.transaction.Transactional;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
@Transactional
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PersistenceSql persistenceSql;

    private User dummyInitUser = User.builder().login("dummyInitUser").password("p".toCharArray())
            .roles(new HashSet<>(Collections.singletonList(Role.USER))).build();

    private User u = User.builder().login("u").password("p".toCharArray())
            .roles(new HashSet<>(Collections.singletonList(Role.USER))).build();

    private User a = User.builder().login("a").password("p".toCharArray())
            .roles(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN))).build();

    private User u1 = User.builder().login("u").password("p".toCharArray())
            .roles(new HashSet<>(Collections.singletonList(Role.ADMIN))).build();

    @Before
    public void init() {
        // Todo start here
        Map<String, String> authorization = persistenceSql.getAllFromAuthorization();
        System.out.println(authorization.size());
        authorization.forEach((u, r) -> System.out.println(u + ":" + r));

        userDao.save(dummyInitUser);
//        userDao.findUserByLogin(u1.getLogin());
    }

    @Test
    public void findUserByLogin() {
        assertThat(userDao.findUserByLogin(dummyInitUser.getLogin()), is(dummyInitUser));
        assertThat(userDao.findUserByLogin(u.getLogin()), is(u));
        assertThat(userDao.findUserByLogin(a.getLogin()), is(a));
    }


    @Test
    public void getNullForNonExistUserOnFindUserByLogin() {
        assertNull(userDao.findUserByLogin("nonExistLogin"));
    }

    @Test
    public void findUserByLoginAndPassword() {
        assertThat(userDao.findUserByLoginAndPassword(dummyInitUser.getLogin(), dummyInitUser.getPassword()),
                is(dummyInitUser));
        assertThat(userDao.findUserByLoginAndPassword(u.getLogin(), u.getPassword()),
                is(u));
        assertThat(userDao.findUserByLoginAndPassword(a.getLogin(), a.getPassword()),
                is(a));
    }

    @Test
    public void getNullForWrongPasswordOnFindUserByLoginAndPassword() {
        assertNull(userDao.findUserByLoginAndPassword(dummyInitUser.getLogin(), "wrongPassword".toCharArray()));
    }

    @Test
    public void getNullForNonExistUserOnFindUserByLoginAndPassword() {
        assertNull(userDao.findUserByLoginAndPassword("nonExistLogin", "p".toCharArray()));
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

    // Todo write test when we create role before
    @Test(expected = JpaObjectRetrievalFailureException.class)
    public void getExceptionOnSaveWithNonExistingRole() {
        assertThat(userDao.save(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(new Role("nonExistingRole")))).build()),
                is(User.builder().login("savedUser").password("savedPassword".toCharArray())
                        .roles(new HashSet<>(Collections.singletonList(new Role("nonExistingRole")))).build()));
    }

    @Test
    public void update() {
        assertThat(userDao.save(User.builder().login(dummyInitUser.getLogin()).password("savedPassword".toCharArray())
                        .roles(dummyInitUser.getRoles()).build()),
                is(dummyInitUser.toBuilder().password("savedPassword".toCharArray()).build())
        );

        assertThat(userDao.findUserByLogin(dummyInitUser.getLogin()),
                is(dummyInitUser.toBuilder().password("savedPassword".toCharArray()).build()));
    }

    @Test
    public void delete() {
        userDao.delete(dummyInitUser.getLogin());
        assertNull(userDao.findUserByLogin(dummyInitUser.getLogin()));
    }

    @Test
    // need to make additional query to get lazy additional from another table during single transaction
    @Transactional
    public void join() {
        User u = userDao.findUserByLogin("u");
        u.getTasks().forEach(System.out::println);
    }
}
