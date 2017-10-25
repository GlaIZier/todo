package ru.glaizier.todo.test.controller.api.user;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
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

@DirtiesContext(classMode = AFTER_CLASS)
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
    public void get201WhenCreateUser() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=testCreatedPassword"))
                .andDo(print())
                .andExpect(status().isCreated())
            .andExpect(content().string("{\"data\":{\"login\":\"testCreatedLogin\"}}"));
    }

    @Test
    public void get400WhenCreateUserWithEmptyLogin() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=&password=testCreatedPassword"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenCreateUserWithEmptyPassword() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password="))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenCreateUserWithLongLogin() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLoginnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn&password=aa"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenCreateUserWithLongPassword() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
