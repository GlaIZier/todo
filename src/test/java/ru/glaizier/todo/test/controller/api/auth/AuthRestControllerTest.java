package ru.glaizier.todo.test.controller.api.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.http.Cookie;

// Almost the same as SpringJUnit4ClassRunner
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class AuthRestControllerTest {

    private static final String LOGIN_PATH = "/api/auth/login";

    private static final String LOGOUT_PATH = "/api/auth/me/logout";

    @Autowired
    private WebApplicationContext context;

    @SpyBean
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
    public void get200WhenAuthenticateUser() throws Exception {
        MvcResult mvcResult = mvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=u&password=p"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content.matches("\\{\"data\":\\{\"login\":\"u\",\"token\":\".+\\..+\\..+\"\\}\\}"), is(true));
    }

    @Test
    public void get400WhenAuthenticateUserWithEmptyLogin() throws Exception {
        mvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=&password=testCreatedPassword"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":{\"code\":400,\"" +
                        "message\":\"Provided user login is empty or null!\"}}"));
    }

    @Test
    public void get400WhenAuthenticateUserWithEmptyPassword() throws Exception {
        mvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password="))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"error\":{\"code\":400,\"" +
                        "message\":\"Provided user password is empty or null!\"}}"));
    }

    @Test
    public void get404WhenAuthenticateUserWithUnknownLogin() throws Exception {
        mvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=testCreatedLogin&password=testCreatedPassword"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("{\"error\":{\"code\":404," +
                        "\"message\":\"User with login testCreatedLogin wasn't found!\"}}"));
    }

    @Test
    public void get401WhenAuthenticateUserWithWrongCredentials() throws Exception {
        mvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("login=u&password=testWrongPassword"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"error\":{\"code\":401," +
                        "\"message\":\"Wrong credentials were provided!\"}}"));
    }

    @Test
    public void get200WhenLogoutUser() throws Exception {
        String token = tokenService.createToken("testUser");
        mvc.perform(post(LOGOUT_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("token=" + token)
                .header(propertiesService.getApiTokenHeaderName(), token)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"response\":{\"code\":200,\"message\":\"OK\"}}"))
                .andExpect(cookie().maxAge(propertiesService.getApiTokenCookieName(), 0));

        Mockito.verify(tokenService).invalidateToken(any());
    }

    @Test
    public void get403WhenLogoutUserWithoutCookie() throws Exception {
        String token = tokenService.createToken("testUser");
        mvc.perform(post(LOGOUT_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("token=" + token)
                .header(propertiesService.getApiTokenHeaderName(), token))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }

    @Test
    public void get403WhenLogoutUserWithoutHeader() throws Exception {
        String token = tokenService.createToken("testUser");
        mvc.perform(post(LOGOUT_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("token=" + token)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }

    @Test
    public void get403WhenCookieNotEqualToHeader() throws Exception {
        String token = tokenService.createToken("testUser");
        mvc.perform(post(LOGOUT_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("token=" + token)
                .header(propertiesService.getApiTokenHeaderName(), "Some value")
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string("{\"error\":{\"code\":403," +
                        "\"message\":\"Missing or non-matching CSRF-token!\"}}"));
    }
}
