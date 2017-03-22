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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Todo start with understanding if i need servletConfig here and if why tests pass when securityconfig is empty
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

    // Todo don't understand why it doesn't work. Looks like this context doesn't contain UserDetailsService. Try this work after enabling csrf
//    @Test
//    @WithMockUser
//    public void testFormBasedAuthentication() throws Exception {
//        mvc
//                .perform(formLogin().user("admin").password("password"))
//                .andDo(print())
//                .andExpect(status().isFound())
//                .andExpect(authenticated().withUsername("admin"));
//    }

}
