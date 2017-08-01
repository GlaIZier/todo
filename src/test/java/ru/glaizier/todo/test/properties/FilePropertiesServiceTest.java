package ru.glaizier.todo.test.properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;
import ru.glaizier.todo.properties.PropertiesService;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServletConfig.class,
        RootConfig.class
})
@WebAppConfiguration
public class FilePropertiesServiceTest {

    @Autowired
    private PropertiesService propertiesService;

    @Test
    public void getApiTokenCookieName() throws Exception {
        assertThat(propertiesService.getApiTokenCookieName(), is("todo-api-token-cookie"));
    }

    @Test
    public void getApiTokenExpireDurationInSeconds() throws Exception {
        assertThat(propertiesService.getApiTokenExpireDurationInSeconds(), is(180));
    }

    @Test
    public void getApiTokenSigningKey() throws Exception {
        assertThat(propertiesService.getApiTokenSigningKey(), is("todo-api-token-signing-key-random"));
    }

    @Test
    public void getApiTokenSessionAttributeName() throws Exception {
        assertThat(propertiesService.getApiTokenSessionAttributeName(), is("todo-api-token-session-attribute"));
    }

}