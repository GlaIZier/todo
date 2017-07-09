package ru.glaizier.todo.test.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.persistence.Persistence;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class PersistenceTest {

    @Autowired
    Persistence p;

    // Todo check cache here?
    @Test
    public void deleteRole() {
        System.out.println(p.findUserByLogin("a"));
        System.out.println(p.findRole("ADMIN"));
        p.deleteRole("ADMIN");
        System.out.println(p.findRole("ADMIN"));
        System.out.println(p.findUserByLogin("a"));
    }

}
