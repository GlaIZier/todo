package ru.glaizier.todo.config.root.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The order is needed to be before WebSecurityConfig, because WebSecurity config matches all paths, but
 * this config matches only /api/**. So if it is after then nothing is going to happen.
 */
@Configuration
@Order(2)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    // These api urls are not protected because they are used for registration and authentication
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .csrf().disable();
    }
}
