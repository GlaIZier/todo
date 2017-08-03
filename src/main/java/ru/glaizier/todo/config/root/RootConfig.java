package ru.glaizier.todo.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import ru.glaizier.todo.config.root.security.SecurityConfig;

@Configuration
// This also enables PropertyPlaceholderBFPP
@PropertySource("classpath:profiles/environment.properties")
@ComponentScan(basePackages = {
        "ru.glaizier.todo.persistence",
        "ru.glaizier.todo.properties",
        "ru.glaizier.todo.init.context"
})
// Register additional configs for root
@Import({
        SecurityConfig.class,
        DbConfig.class
})
public class RootConfig {
}
