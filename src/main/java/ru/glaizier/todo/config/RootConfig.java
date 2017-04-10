package ru.glaizier.todo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {
        "ru.glaizier.todo.dao",
        "ru.glaizier.todo.security"
})
@PropertySource("classpath:profiles/local.properties")
public class RootConfig {
}
