package ru.glaizier.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.glaizier.config.SecurityConfig;
import ru.glaizier.config.ServletConfig;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// Todo start with understanding if i need servletConfig here and if why tests pass when securityconfig is empty
// Todo start here understand how Spring Security Test works
// Todo check how RequestPostProcessors work
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SecurityConfig.class,
        ServletConfig.class
})
@WebAppConfiguration
public class SecurityTest {
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
    public void shouldGetRootApiUnauthenticated() throws Exception {
        mvc
                .perform(get("/api/v1/tasks"))
                .andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());
    }

    @Test
    public void shouldGetRootApiAuthenticatedWhenUserIsPresent() throws Exception {
        mvc
                .perform(get("/api/v1/tasks").with(user("u")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("u"));
    }

    @Test
    public void shouldGetTasksUnauthenticatedAndGotoLogin() throws Exception {
        mvc
                .perform(get("/tasks"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", equalTo("http://localhost/login")))
                .andExpect(unauthenticated());
    }

    @Test
    public void shouldGetTasksAuthenticatedAndGotoLogin() throws Exception {
        mvc
                .perform(get("/tasks").with(user("u")))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("u"));
    }

//    @Test
//    public void shouldAvoidWrongPassword() throws Exception {
//        mvc
//                .perform(formLogin())
//                .andExpect(authenticated());
//    }

}
