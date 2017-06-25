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
import ru.glaizier.todo.dao.UserDao;
import ru.glaizier.todo.domain.User;

import javax.transaction.Transactional;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    private User dummyInitUser = User.builder().login("dummyInitUser").password("p".toCharArray())
            /*.roles(Collections.singletonList(USER))*/.build();

            /*
    @Before
    public void init() {
        userDao.save(dummyInitUser);
    }

    @Test
    public void findUserByLogin() {
        assertThat(userDao.findUserByLogin(dummyInitUser.getLogin()), is(dummyInitUser));
    }

    @Test
    public void getNullForNonExistUserOnFindUserByLogin() {
        assertNull(userDao.findUserByLogin("nonExistLogin"));
    }

    @Test
    public void findUserByLoginAndPassword() {
        assertThat(userDao.findUserByLoginAndPassword(dummyInitUser.getLogin(), dummyInitUser.getPassword()),
                is(dummyInitUser));
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
        assertThat(userDao.save(new User("savedUser", "savedPassword".toCharArray())),
                is(new User("savedUser", "savedPassword".toCharArray())));
        assertThat(userDao.findUserByLogin("savedUser"), is(new User("savedUser", "savedPassword".toCharArray())));
    }

    @Test
    public void update() {
        assertThat(userDao.save(new User(dummyInitUser.getLogin(), "savedPassword".toCharArray())),
                is(new User(dummyInitUser.getLogin(), "savedPassword".toCharArray())));
        assertThat(userDao.findUserByLogin(dummyInitUser.getLogin()),
                is(new User(dummyInitUser.getLogin(), "savedPassword".toCharArray())));
    }

    @Test
    public void delete() {
        userDao.delete(dummyInitUser.getLogin());
        assertNull(userDao.findUserByLogin(dummyInitUser.getLogin()));
    }
    */

    @Test
    // need to make additional query to get lazy additional from another table during single transaction
    @Transactional
    public void join() {
        User u = userDao.findUserByLogin("u");
        u.getTasks().forEach(System.out::println);
    }
}
