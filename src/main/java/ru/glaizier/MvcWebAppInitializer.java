package ru.glaizier;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.glaizier.config.RootConfig;
import ru.glaizier.config.SecurityConfig;
import ru.glaizier.config.ServletConfig;

// todo add aop logging
public class MvcWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    // Register securityconfig in application context connected with the web application so it will be shared between all servlets
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                RootConfig.class,
                SecurityConfig.class
        };
    }

    @Override
    // Register configs for dispatcher servlets in web app. So we can have different dispatcher servlets and associated beans with them.
    // These beans are not shared between each other. In this app we have only one dispatcher servlet.
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                ServletConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
