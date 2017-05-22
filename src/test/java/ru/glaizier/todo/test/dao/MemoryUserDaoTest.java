package ru.glaizier.todo.test.dao;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static ru.glaizier.todo.domain.Role.USER;

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
import ru.glaizier.todo.dao.UserDao;
import ru.glaizier.todo.domain.User;

import java.util.Collections;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class MemoryUserDaoTest {

    @Autowired
    private UserDao memoryUserDao;

    private User dummyInitUser = User.builder().login("dummyInitUser").password("p".toCharArray())
            .roles(Collections.singletonList(USER)).build();

    @Before
    public void init() {
        memoryUserDao.addUser(dummyInitUser);
    }

    @Test
    public void createUserOnCreateUser() {
        User dummyUser = User.builder().login("dummyUser").password("p".toCharArray())
                .roles(Collections.singletonList(USER)).build();
        memoryUserDao.addUser(dummyUser);
        assertThat(memoryUserDao.getUser(dummyUser.getLogin()), is(dummyUser));
    }

    @Test
    public void getExistUserOnGetUser() {
        assertThat(memoryUserDao.getUser(dummyInitUser.getLogin()), is(dummyInitUser));
    }

    @Test
    public void getNullForNonExistUserOnGetUser() {
        assertNull(memoryUserDao.getUser("NonExistLogin"));
    }

    @Test
    public void getExistUserOnGetUserWithPassword() {
        assertThat(memoryUserDao.getUserWithPassword(dummyInitUser.getLogin(), dummyInitUser.getPassword()),
                is(dummyInitUser));
    }

    @Test
    public void getNullForWrongPasswordOnGetUserWithPassword() {
        assertNull(memoryUserDao.getUserWithPassword(dummyInitUser.getLogin(), "wrongPassword".toCharArray()));
    }

    @Test
    public void getNullForNonExistUserOnGetUserWithPassword() {
        assertNull(memoryUserDao.getUser("NonExistLogin"));
    }

    @Test
    public void getRemovedUserOnRemoveUser() {
        assertThat(memoryUserDao.removeUser(dummyInitUser.getLogin()), is(dummyInitUser));
    }

    @Test
    public void getNullForNonExistUserOnRemoveUser() {
        assertNull(memoryUserDao.removeUser("NonExistLogin"));
    }

    @Test
    public void getSetSizeMoreOrEqual1OnGetUsers() {
        assertThat(memoryUserDao.getUsers().size(), greaterThan(1));
    }
}
