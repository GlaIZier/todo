package ru.glaizier.todo;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.glaizier.todo.config.root.RootConfig;
import ru.glaizier.todo.config.servlet.ServletConfig;

// Todo add aop logging
// Todo think about exception handling architecture
public class MvcWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

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
}
