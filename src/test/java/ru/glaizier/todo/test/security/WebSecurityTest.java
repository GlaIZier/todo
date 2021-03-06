package ru.glaizier.todo.test.security;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.properties.PropertiesService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class WebSecurityTest {

    @Autowired
    private WebApplicationContext context;

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
    public void getTasksUnauthenticatedAndRedirectToLogin() throws Exception {
        mvc
            .perform(get("/web/tasks").secure(true))
                .andDo(print())
                .andExpect(status().isFound())
            .andExpect(redirectedUrl("http://localhost/web/login"))
            .andExpect(header().string("Location", equalTo("http://localhost/web/login")))
                .andExpect(unauthenticated());
    }

    @Test
    public void getTasksSlashUnauthenticatedAndRedirectToLogin() throws Exception {
        mvc
            .perform(get("/web/tasks/").secure(true))
                .andDo(print())
                .andExpect(status().isFound())
            .andExpect(redirectedUrl("http://localhost/web/login"))
            .andExpect(header().string("Location", equalTo("http://localhost/web/login")))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(value = "fake")
    public void getTasksAuthenticatedWhenFakeUserIsPresent() throws Exception {
        mvc
            .perform(get("/web/tasks").secure(true))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("fake").withRoles("USER"));
    }

    @Test
    public void getLogoutUnauthenticatedAndRedirectToRoot() throws Exception {
//        mvc
//            .perform(logout())
//            .andDo(print())
//            .andExpect(status().isFound())
//            .andExpect(redirectedUrl("/"))
//            .andExpect(header().string("Location", equalTo("/")))
//            .andExpect(unauthenticated())
//            .andExpect(cookie().maxAge(propertiesService.getApiTokenCookieName(), 0))
//            .andExpect(cookie().maxAge("todo-remember-me-cookie", 0));

        mvc
            .perform(post("/web/logout").with(csrf()).secure(true))
                .andDo(print())
                .andExpect(status().isFound())
            .andExpect(redirectedUrl("/web"))
            .andExpect(header().string("Location", equalTo("/web")))
                .andExpect(unauthenticated())
                .andExpect(cookie().maxAge(propertiesService.getApiTokenCookieName(), 0))
                .andExpect(cookie().maxAge("todo-remember-me-cookie", 0));
    }

    @Test
    // Spring security automatically inject UserDetailsService from SecurityConfig because
    // WithUserDetailsSecurityContextFactory is annotated with @Autowired
    @IfProfileValue(name = "spring.profiles.active", values = {"memory", "default"})
    public void postLoginAuthenticatedAndRedirectToRoot() throws Exception {
//        mvc
//                .perform(formLogin().userParameter("user").user("u").password("p"))
//                .andDo(print())
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/"))
//                .andExpect(header().string("Location", equalTo("/")))
//                .andExpect(authenticated().withUsername("u").withRoles("USER"));

        mvc
            .perform(post("/web/login").param("user", "u").param("password", "p")
                .with(csrf()).secure(true))
                .andDo(print())
                .andExpect(status().isFound())
            .andExpect(redirectedUrl("/web"))
            .andExpect(header().string("Location", equalTo("/web")))
                .andExpect(authenticated().withUsername("u").withRoles("USER"));
    }

    @Test
    @IfProfileValue(name = "spring.profiles.active", values = {"memory", "default"})
    public void postLoginAdminAuthenticatedAndRedirectToRoot() throws Exception {
//        mvc
//                .perform(formLogin().userParameter("user").user("a").password("p"))
//                .andDo(print())
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/"))
//                .andExpect(header().string("Location", equalTo("/")))
//                .andExpect(authenticated().withUsername("u").withRoles("USER", "ADMIN"));

        mvc
            .perform(post("/web/login").param("user", "a").param("password", "p")
                .with(csrf()).secure(true))
                .andDo(print())
                .andExpect(status().isFound())
            .andExpect(redirectedUrl("/web"))
            .andExpect(header().string("Location", equalTo("/web")))
                .andExpect(authenticated().withUsername("a").withRoles("USER", "ADMIN"));
    }

    @Test
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
    @IfProfileValue(name = "spring.profiles.active", values = {"memory", "default"})
    public void postLoginAuthenticatedWithRememberMeCookieAndRedirectToRoot() throws Exception {
        // use post() instead of formlogin() because formlogin() doesn't provide method to attach remember-me param
        mvc
            .perform(post("/web/login").param("user", "u").param("password", "p")
                    .param("remember-me", "on").with(csrf()).secure(true))
                .andDo(print())
                .andExpect(status().isFound())
            .andExpect(redirectedUrl("/web"))
            .andExpect(header().string("Location", equalTo("/web")))
                .andExpect(authenticated().withUsername("u").withRoles("USER"))
                .andExpect(cookie().exists("todo-remember-me-cookie"));
    }
}
