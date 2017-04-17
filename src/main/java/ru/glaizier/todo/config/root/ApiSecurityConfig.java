package ru.glaizier.todo.config.root;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(2)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
}
