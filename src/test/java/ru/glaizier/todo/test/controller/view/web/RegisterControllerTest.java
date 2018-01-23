package ru.glaizier.todo.test.controller.view.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.controller.view.web.RegisterController;
import ru.glaizier.todo.persistence.Persistence;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class RegisterControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void show() throws Exception {
        mvc.perform(get("/web/register/").secure(true))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    // Also we can do this with @MockBean from Spring Boot Test package
    public void processRegistrationOk() throws Exception {
        Persistence persistence = mock(Persistence.class);
        RegisterController controller =
                new RegisterController(persistence);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(post("/web/register/")
                .param("login", "login")
                .param("password", "password"))
            .andExpect(redirectedUrl("/web"));
        verify(persistence).saveUser("login", "password".toCharArray());
    }

    @Test
    // Also we can do this with @MockBean from Spring Boot Test package
    public void processRegistrationValidationFailed() throws Exception {
        Persistence persistence = mock(Persistence.class);
        RegisterController controller =
                new RegisterController(persistence);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/web/register/")
                .param("login", "")
                .param("password", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        verify(persistence, never()).saveUser(any(), any());
    }

}
