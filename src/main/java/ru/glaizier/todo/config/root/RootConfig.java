package ru.glaizier.todo.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import ru.glaizier.todo.config.root.security.SecurityConfig;

@Configuration
@ComponentScan(basePackages = {
        "ru.glaizier.todo.dao",
        "ru.glaizier.todo.properties"
})
// This also enables PropertyPlaceholderBFPP
@PropertySource("classpath:profiles/environment.properties")
// Register additional configs for root
@Import({
        SecurityConfig.class
})
public class RootConfig {
}
