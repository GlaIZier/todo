package ru.glaizier.todo.test.controller.api.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class UserRestControllerTest {

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

    /**
     * Create
     */
    @Test
    public void get201WhenCreateTask() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=testCreatedPassword")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"data\":\"testCreatedLogin\"}"));
    }

    @Test
    public void get400WhenCreateTaskWithEmptyLogin() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=&password=testCreatedPassword")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenCreateTaskWithEmptyPassword() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
