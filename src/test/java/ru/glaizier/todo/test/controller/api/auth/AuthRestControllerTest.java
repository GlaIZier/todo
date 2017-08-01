package ru.glaizier.todo.test.controller.api.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;

// Almost the same as SpringJUnit4ClassRunner
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class AuthRestControllerTest {
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
    public void get200WhenAuthenticateUser() throws Exception {
        MvcResult mvcResult = mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=u&password=p")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content.matches("\\{\"data\":\\{\"login\":\"u\",\"token\":\".+\\..+\\..+\"\\}\\}"), is(true));
    }

    @Test
    public void get400WhenAuthenticateUserWithEmptyLogin() throws Exception {
        mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=&password=testCreatedPassword")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":{\"code\":400,\"" +
                        "message\":\"Provided user login is empty or null!\"}}"));
    }

    @Test
    public void get400WhenAuthenticateUserWithEmptyPassword() throws Exception {
        mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":{\"code\":400,\"" +
                        "message\":\"Provided user password is empty or null!\"}}"));
    }

    @Test
    public void get404WhenAuthenticateUserWithUnknownLogin() throws Exception {
        mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=testCreatedPassword")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"User with login testCreatedLogin wasn't found!\"}}"));
    }

    @Test
    public void get401WhenAuthenticateUserWithWrongCredentials() throws Exception {
        mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=u&password=testWrongPassword")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"error\":{\"code\":401," +
                        "\"message\":\"Wrong credentials were provided!\"}}"));
    }
}
