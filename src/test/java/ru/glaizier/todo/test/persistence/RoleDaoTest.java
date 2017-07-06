package ru.glaizier.todo.test.persistence;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
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
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.persistence.role.RoleDao;

import javax.transaction.Transactional;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
@Transactional
public class RoleDaoTest {

    @Autowired
    private RoleDao roleDao;

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
}
