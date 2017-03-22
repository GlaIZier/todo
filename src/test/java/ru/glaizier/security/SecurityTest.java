package ru.glaizier.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.glaizier.config.SecurityConfig;
import ru.glaizier.config.ServletConfig;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Todo start here understand how Spring Security Test works
// Todo check how RequestPostProcessors work
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        SecurityConfig.class
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
    // Todo add tests with wrong passwords for api and tasks with userdetails
    // Todo add test for remember-me cookie
    public void getApiUnauthenticatedAndReturn401() throws Exception {
        mvc
                .perform(get("/api/v1/tasks"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());
    }

    @Test
    public void getApiAuthenticatedWhenUserIsPresent() throws Exception {
        mvc
                .perform(get("/api/v1/tasks").with(user("u")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("u"));
    }

    @Test
    public void getTasksUnauthenticatedAndRedirectToLogin() throws Exception {
        mvc
                .perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(header().string("Location", equalTo("http://localhost/login")))
                .andExpect(unauthenticated());
    }

    @Test
    public void getTasksAuthenticatedWhenUserIsPresent() throws Exception {
        mvc
                .perform(get("/tasks").with(user("u")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("u"));
    }

    @Test
    public void getLogoutUnauthenticatedAndRedirectToRoot() throws Exception {
        mvc
                .perform(logout())
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(header().string("Location", equalTo("/")))
                .andExpect(unauthenticated());
    }

    @Test
    // inject UserDetailsService from SecurityConfig
    @WithUserDetails
    public void postLoginAuthenticatedAndRedirectToRoot() throws Exception {
        mvc
                .perform(formLogin().userParameter("login").user("user").password("password"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(header().string("Location", equalTo("/")))
                .andExpect(authenticated().withUsername("user"));
    }

    @Test
    // inject UserDetailsService from SecurityConfig
    @WithUserDetails
    public void postLoginUnauthenticatedBecauseOfWrongPasswordOrLogin() throws Exception {
        mvc
                .perform(formLogin().userParameter("login").user("user").password("password1"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(unauthenticated());

        mvc
                .perform(formLogin().userParameter("login").user("user1").password("password"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(unauthenticated());

    }
}
