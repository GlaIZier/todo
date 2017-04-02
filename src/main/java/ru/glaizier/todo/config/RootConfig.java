package ru.glaizier.todo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "ru.glaizier.todo.dao",
        "ru.glaizier.todo.security"
})
public class RootConfig {
}
