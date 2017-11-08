package ru.glaizier.todo.test.controller.api.task;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenService;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
// Make this transactional because we need to rollback the db transaction on save methods not to save anything to db
@Transactional
// Todo make tests not reliable on previously saved test
public class TaskRestControllerTest {

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

    /**
     * Read collection
     */
    @Test
    public void get200WhenGetTasks() throws Exception {
        // We can get empty list but it's OK 200 for rest get collection
        String token = tokenService.createToken("u");

        mvc.perform(get("/api/me/tasks").cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":" +
                        "[{\"data\":{\"id\":1,\"login\":\"u\",\"todo\":\"todo1\"},\"_link\":{\"self\":\"/api/me/tasks/1\"}}," +
                        "{\"data\":{\"id\":2,\"login\":\"u\",\"todo\":\"todo2\"},\"_link\":{\"self\":\"/api/me/tasks/2\"}}]}"));
    }

    @Test
    public void get404WhenGetTasksForUnknownUser() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(get("/api/me/tasks").cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Read id
     */
    @Test
    public void get200WhenGetTask() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(get("/api/me/tasks/1").cookie(new Cookie(propertiesService.getApiTokenCookieName(),
                token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":{\"id\":1,\"login\":\"u\",\"todo\":\"todo1\"}}"));

        token = tokenService.createToken("a");

        mvc.perform(get("/api/me/tasks/3").cookie(new Cookie(propertiesService.getApiTokenCookieName(),
                token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":{\"id\":3,\"login\":\"a\",\"todo\":\"todo1\"}}"));
    }

    @Test
    public void get404WhenGetUnknownIdTask() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(get("/api/me/tasks/100").cookie(new Cookie(propertiesService.getApiTokenCookieName(),
                token)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"Task for user u with id 100 hasn't been found!\"}}"));
    }

    @Test
    public void get404WhenGetTaskForUnknownUser() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(get("/api/me/tasks/1").cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"Task for user dummyLogin with id 1 hasn't been found!\"}}"));
    }

    @Test
    public void get403WhenGetTaskWithExistingIdButWrongUser() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(get("/api/me/tasks/3").cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"User with login u doesn't have rights to access task with 3 id!\"}}"));
    }

    /**
     * Create
     */
    @Test
    public void get201WhenCreateTask() throws Exception {
        String token = tokenService.createToken("u");


        mvc.perform(post("/api/me/tasks")
                .param("todo", "todoCreatedWithinTaskTest3")
//                .content("todo=todoCreatedWithinTaskTest3")
                .contentType(MediaType.APPLICATION_JSON_UTF8)

                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/me/tasks/4"))
                .andExpect(content().string("{\"data\":{\"id\":4,\"login\":\"u\"," +
                        "\"todo\":\"todoCreatedWithinTaskTest3\"},\"_link\":{\"self\":\"/api/me/tasks/4\"}}"));
    }

    @Test
    public void get403WhenCreateTaskWithoutCsrfHeader() throws Exception {
        String token = tokenService.createToken("u");


        mvc.perform(post("/api/me/tasks")
                .param("todo", "todoCreatedWithinTaskTest3")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }

    @Test
    public void get403WhenCreateTaskWithWrongCsrfHeader() throws Exception {
        String token = tokenService.createToken("u");


        mvc.perform(post("/api/me/tasks")
                .param("todo", "todoCreatedWithinTaskTest3")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), "wrongToken"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }

    @Test
    public void get404WhenCreateTaskForUnknownUser() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(post("/api/me/tasks")
                .param("todo", "todo1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404,\"" +
                        "message\":\"Task creation failed! Login dummyLogin hasn't been found to create task for!\"}}"));
    }

    /**
     * Update
     */
    @Test
    public void get200WhenUpdateTask() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(put("/api/me/tasks/1")
                .param("todo", "todoUpdatedWithinTaskTest1")
//                .content("todoUpdatedWithinTaskTest1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":{\"id\":1,\"login\":\"u\"," +
                        "\"todo\":\"todoUpdatedWithinTaskTest1\"}}"));
    }

    @Test
    public void get403WhenUpdateTaskWithoutCsrfHeader() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(put("/api/me/tasks/1")
                .param("todo", "todoUpdatedWithinTaskTest1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }

    @Test
    public void get404WhenUpdateUnknownIdTask() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(put("/api/me/tasks/100")
                .param("todo", "todoUpdatedWithinTaskTest100")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"Task for user u with id 100 hasn't been found!\"}}"));
    }

    @Test
    public void get404WhenUpdateTaskForUnknownUser() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(put("/api/me/tasks/1")
                .param("todo", "todoUpdatedWithinTaskTest1")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"Task for user dummyLogin with id 1 hasn't been found!\"}}"));
    }

    /**
     * Delete
     */
    @Test
    public void get200WhenDeleteTask() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(delete("/api/me/tasks/1")
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"data\":{\"id\":1,\"login\":\"u\",\"todo\":\"todo1\"}}"));
    }

    @Test
    public void get403WhenDeleteTaskWithoutCsrfHeader() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(delete("/api/me/tasks/1")
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }

    @Test
    public void get404WhenDeleteUnknownIdTask() throws Exception {
        String token = tokenService.createToken("u");

        mvc.perform(delete("/api/me/tasks/100")
                .param("todo", "todoUpdatedWithinTaskTest100")
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"Task for user u with id 100 hasn't been found!\"}}"));
    }

    @Test
    public void get404WhenDeleteTaskForUnknownUser() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(delete("/api/me/tasks/1")
                .param("todo", "todoUpdatedWithinTaskTest1")
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"Task for user dummyLogin with id 1 hasn't been found!\"}}"));
    }
}
