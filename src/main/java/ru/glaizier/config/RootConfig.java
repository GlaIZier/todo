package ru.glaizier.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "ru.glaizier.dao",
        "ru.glaizier.security"
})
public class RootConfig {
}
