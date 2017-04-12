package ru.glaizier.todo.test.config;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.glaizier.todo.config.RootConfig;
import ru.glaizier.todo.config.SecurityConfig;
import ru.glaizier.todo.config.ServletConfig;


// Todo start here understand how Spring Security Test works
// Todo check how RequestPostProcessors work
@RunWith(SpringJUnit4ClassRunner.class)
// Todo try here webappinitializer
@ContextConfiguration(classes = {
        SecurityConfig.class,
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class SecurityConfigIntegrationTest {

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
    public void getApiUnauthenticatedAndReturn401() throws Exception {
        mvc
                .perform(get("/api/v1/tasks"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(value = "fake")
    public void getApiAuthenticatedWhenFakeUserIsPresent() throws Exception {
        mvc
                .perform(get("/api/v1/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("fake").withRoles("USER"));
    }

    @Test
    public void getApiAuthenticatedWhenRealUserIsPresent() throws Exception {
        mvc
                .perform(get("/api/v1/tasks").with(httpBasic("u", "p")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("u").withRoles("USER"));
    }

    @Test
    public void getApiAuthenticatedWhenRealAdminIsPresent() throws Exception {
        mvc
                .perform(get("/api/v1/tasks").with(httpBasic("a", "p")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("a").withRoles("USER", "ADMIN"));
    }

    @Test
    public void getApiUnauthenticatedBecauseOfWrongPasswordOrLogin() throws Exception {

        mvc
                .perform(get("/api/v1/tasks").with(httpBasic("u", "p1")))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        mvc
                .perform(get("/api/v1/tasks").with(httpBasic("u1", "p")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
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
    @WithMockUser(value = "fake")
    public void getTasksAuthenticatedWhenFakeUserIsPresent() throws Exception {
        mvc
                .perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("fake").withRoles("USER"));
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
    // Spring security automatically inject UserDetailsService from SecurityConfig because
    // WithUserDetailsSecurityContextFactory is annotated with @Autowired
    @WithUserDetails(value = "u")
    public void postLoginAuthenticatedAndRedirectToRoot() throws Exception {
        mvc
                .perform(formLogin().userParameter("user").user("u").password("p"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(header().string("Location", equalTo("/")))
                .andExpect(authenticated().withUsername("u").withRoles("USER"));
    }

    @Test
    // Spring security automatically inject UserDetailsService from SecurityConfig because
    // WithUserDetailsSecurityContextFactory is annotated with @Autowired
    @WithUserDetails(value = "u")
    public void postLoginAdminAuthenticatedAndRedirectToRoot() throws Exception {
        mvc
                .perform(formLogin().userParameter("user").user("a").password("p"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(header().string("Location", equalTo("/")))
                .andExpect(authenticated().withUsername("a").withRoles("USER", "ADMIN"));
    }

    @Test
    // Spring security automatically inject UserDetailsService from SecurityConfig because
    // WithUserDetailsSecurityContextFactory is annotated with @Autowired
    @WithUserDetails(value = "u")
    public void postLoginUnauthenticatedBecauseOfWrongPasswordOrLogin() throws Exception {
        mvc
                .perform(formLogin().userParameter("user").user("u").password("p1"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(unauthenticated());

        mvc
                .perform(formLogin().userParameter("user").user("u1").password("p"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }

    @Test
    // we can check if cookie is present even without providing user and password
    public void postLoginAuthenticatedWithRememberMeCookieAndRedirectToRoot() throws Exception {
        // use post() instead of formlogin() because formlogin() doesn't provide method to attach remember-me param
        mvc
                .perform(post("/login").param("user", "u").param("password", "p")
                        .param("remember-me", "on").with(csrf()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(header().string("Location", equalTo("/")))
                .andExpect(authenticated().withUsername("u").withRoles("USER"))
                .andExpect(cookie().exists("todo-remember-me-cookie"));
    }
}
