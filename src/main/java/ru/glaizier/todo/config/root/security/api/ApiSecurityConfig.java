package ru.glaizier.todo.config.root.security.api;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * The order is needed to be before WebSecurityConfig, because WebSecurity config matches all paths, but
 * this config matches only /api/**. So if it is after then nothing is going to happen.
 */
@Configuration
@Order(2)
public class ApiSecurityConfig extends ApiSecurityConfigAdapter {

    @Override
    // These api urls are not protected because they are used for registration and authentication
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**")
            .cors().and()
            .csrf().disable();
    }
}
