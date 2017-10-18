package ru.glaizier.todo.test.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.controller.view.TaskController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class MethodSecurityTest {

    @Autowired
    private TaskController taskController;

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void forbidGetTasksCallWithoutAuthentication() {
        taskController.getTasks(null, null);
    }

}
