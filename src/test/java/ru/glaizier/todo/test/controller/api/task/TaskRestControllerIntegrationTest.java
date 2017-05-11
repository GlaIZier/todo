package ru.glaizier.todo.test.controller.api.task;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.glaizier.todo.domain.Task;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.http.Cookie;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class TaskRestControllerIntegrationTest {

    private static final String testUri = "/api/me/tasks";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PropertiesService propertiesService;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void get200WhenGetTasks() throws Exception {
        // We can get empty list but it's OK 200 for rest get collection
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(get("/api/me/tasks").cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void get400WhenCreateTaskForUnknownUser() throws Exception {
        // We can get empty list but it's OK 200 for rest get collection
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(post("/api/me/tasks")
                .content(new ObjectMapper().writeValueAsString(new Task(1, "todo1")))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf())
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}
