package ru.glaizier.todo.test.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.Cookie;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class ApiSecurityIntegrationTest {

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
    public void return401WhenTokenCookieIsMissing() throws Exception {
        // need to provide dummy cookie or we will get NullPointerException while getting cookies in filter:
        // req.getCookies
        mvc.perform(get(testUri).cookie(new Cookie("dummy", "dummy")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void get401WhenTokenSignatureIsInvalid() throws Exception {
        String token = tokenService.createToken("dummyLogin");
        String invalidToken = token.substring(0, token.length() - 3);
        invalidToken = invalidToken + token.charAt(token.length() - 2) + token.charAt(token.length() - 1) +
                token.charAt(token.length() - 1);

        mvc.perform(get(testUri).cookie(new Cookie(propertiesService.getApiTokenCookieName(), invalidToken)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    //    @Test
//    public void get400WhenTokenIsCorrupted() {
//        String token = agentJWTEncoder.encodeSystemAgent("testId", "testMnemonicId");
//        // corrupted token because we replace last = with prev symbol
//        String corruptedToken = token.substring(0, token.length() - 2);
//        corruptedToken = corruptedToken + token.charAt(token.length() - 1) + token.charAt(token.length() - 2);
//
//        ClientHttpRequestInterceptor interceptor = new ApiClientHeaderAdder(apiAuthTokenHeaderName, corruptedToken);
//        restTemplate.getRestTemplate().setInterceptors(Collections.singletonList(interceptor));
//
//        ResponseEntity<String> deliveryResponse = restTemplate.getForEntity(
//                DELIVERY_LICENSES_OBOOK_ONIX_LICENSE_ID_URL, String.class);
//        assertThat(deliveryResponse.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    }
//
    @Test
    @Ignore
    // Add mockito amd avoid NPE in taskDao
    public void get200WhenTokenIsOk() throws Exception {
        String token = tokenService.createToken("dummyLogin");

        mvc.perform(get(testUri).cookie(new Cookie(propertiesService.getApiTokenCookieName(), token)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
