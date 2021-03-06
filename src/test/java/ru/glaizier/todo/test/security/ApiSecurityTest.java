package ru.glaizier.todo.test.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    ServletConfig.class,
    RootConfig.class
})
@WebAppConfiguration
public class ApiSecurityTest {

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
    public void get401WhenTokenCookieIsMissing() throws Exception {
        // need to provide dummy cookie or we will get NullPointerException while getting cookies in filter:
        // req.getCookies
        mvc.perform(get(testUri).cookie(new Cookie("dummy", "dummy")))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void get401WhenTokenSignatureIsInvalid() throws Exception {
        String token = tokenService.createToken("dummyLogin");
        // exchange prelast and preprelast chars, because last char is =
        String invalidToken = token.substring(0, token.length() - 3);
        invalidToken = invalidToken + token.charAt(token.length() - 2) + token.charAt(token.length() - 1) +
            token.charAt(token.length() - 1);

        mvc.perform(get(testUri).cookie(new Cookie(propertiesService.getApiTokenCookieName(), invalidToken)))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    @IfProfileValue(name = "spring.profiles.active", values = {"memory", "default"})
    public void get200WhenTokenIsOk() throws Exception {
        // We can get empty list but it's OK 200 for rest get collection
        String token = tokenService.createToken("u");

        mvc.perform(get(testUri).cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void get404WhenTokenIsOkForUnknownUser() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(get(testUri).cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    /**
     * To make Spring's {@link org.springframework.web.filter.CorsFilter} work we have to add "Origin" header
     * or it won't do anything with a request
     */
    @Test
    public void getCorsHeaderForLogin() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(
            post("/api/auth/login")
                .header(HttpHeaders.ORIGIN, "http://externalhost.com"))
            .andDo(print())
            // value is not * because of the allowCredentials true
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://externalhost.com"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    public void getCorsHeaderForPostTasks() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(
            post(testUri)
                .header(HttpHeaders.ORIGIN, "http://externalhost.com")
                .header(propertiesService.getApiTokenHeaderName(), token))
            .andDo(print())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://externalhost.com"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    public void getCorsHeaderForPreflightRequestForPostTasks() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(
            options(testUri)
                .cookie(new Cookie(propertiesService.getApiTokenCookieName(), token))
                .header(HttpHeaders.ORIGIN, "http://externalhost.com")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                    .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, propertiesService.getApiTokenHeaderName()))
            .andDo(print())
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://externalhost.com"))
            .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, propertiesService.getApiTokenHeaderName()));

    }
}
