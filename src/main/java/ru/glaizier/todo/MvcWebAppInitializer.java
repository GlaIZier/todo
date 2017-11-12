package ru.glaizier.todo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.InputStream;
import java.util.Properties;

// Todo move the whole application to docker to run it with one command
@Slf4j
public class MvcWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String SPRING_PROFILE_PROPERTY_NAME = "spring.profiles.active";
    private static final String PROPERTIES_CLASSPATH_LOCATION = "profiles/environment.properties";

    @Override
    // Register in application context connected with the whole web application so it will be shared
    // between all servlets
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                RootConfig.class
        };
    }

    @Override
    // Register servlet configs for dispatcher servlets in web app, creating different servlet contexts.
    // So we can have different dispatcher servlets, containers and associated beans with it.
    // These beans are not shared between each other. In this app we have only one dispatcher servlet and one servlet container.
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                ServletConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext context) throws ServletException {
        super.onStartup(context);

        // read as a system property. If it is null then read from property file
        String activeProfile = System.getProperty(SPRING_PROFILE_PROPERTY_NAME);
        if (activeProfile == null) {
            log.info("Spring profile hasn't been found in system properties");
            activeProfile = readActiveProfileFromPropertyFile();
            if (activeProfile != null)
                log.info("Found '{}' Spring profile in properties file", activeProfile);
            else
                log.warn("Spring profile hasn't been found in properties file");
        } else {
            log.info("Found '{}' Spring profile in system properties", activeProfile);
        }

        if (activeProfile != null) {
            context.setInitParameter(SPRING_PROFILE_PROPERTY_NAME, activeProfile);
            log.info("Spring profile '{}' has been set in servlet context", activeProfile);
        } else
            log.warn("Spring profile hasn't been found neither in system properties, neither in properties file. " +
                    "Let Spring deal with it by itself");
    }

    // Can't use PropertiesService because we don't have Spring context yet
    private String readActiveProfileFromPropertyFile() {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTIES_CLASSPATH_LOCATION)) {
            properties.load(is);
            return properties.getProperty(SPRING_PROFILE_PROPERTY_NAME);
        } catch (Exception e) {
            log.error("Error while reading active profile from property file: {}", e.getMessage(), e);
            return null;
        }
    }
}
