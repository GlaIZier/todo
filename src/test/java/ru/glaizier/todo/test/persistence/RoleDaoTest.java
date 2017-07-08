package ru.glaizier.todo.test.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.persistence.role.RoleDao;
import ru.glaizier.todo.persistence.user.UserDao;

import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashSet;

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
public class RoleDaoTest {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserDao userDao;

    private Role dummyRole = new Role("dummyRole");

    private Role user = Role.USER;

    private Role admin = Role.ADMIN;


    @Before
    public void init() {
        roleDao.save(dummyRole);
    }

    @Test
    public void findRoleByRole() {
        assertThat(roleDao.findRoleByRole(dummyRole.getRole()), is(dummyRole));
        assertThat(roleDao.findRoleByRole(user.getRole()), is(user));
        assertThat(roleDao.findRoleByRole(admin.getRole()), is(admin));
    }

    @Test
    public void getNullForNonExistRoleOnFindRoleByRole() {
        assertNull(roleDao.findRoleByRole("nonExistingRole"));
    }


    @Test
    public void findAll() {
        assertThat(roleDao.findAll().size(), is(3));
    }

    @Test
    public void save() {
        assertThat(roleDao.save(new Role("savedRole")), is(new Role("savedRole")));
        assertThat(roleDao.findRoleByRole("savedRole"), is(new Role("savedRole")));
    }

    @Test
    public void delete() {
        assertNotNull(roleDao.findRoleByRole(dummyRole.getRole()));
        roleDao.delete(dummyRole.getRole());
        assertNull(roleDao.findRoleByRole(dummyRole.getRole()));
    }

    @Test
    public void deleteRoleWithUserAssigned() throws Exception {
        User a = User.builder().login("a").password("p".toCharArray())
                .roles(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN))).build();
        assertThat(userDao.findUserByLogin("a"), is(a));
        roleDao.delete(admin.getRole());
        assertNull(roleDao.findRoleByRole(admin.getRole()));
//        assertThat(userDao.findUserByLogin("a"), is(a.toBuilder().roles(new HashSet<>(Arrays.asList(Role.USER))).build()));
    }
}
