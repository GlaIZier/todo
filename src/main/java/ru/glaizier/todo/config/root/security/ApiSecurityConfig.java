package ru.glaizier.todo.config.root.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.glaizier.todo.security.filter.ApiTokenAuthenticationFilter;

import javax.servlet.Filter;

/**
 * The order is needed to be before WebSecurityConfig, because WebSecurity config matches all paths, but
 * this config matches only /api/**. So if it is after then nothing is going to happen.
 * Also we can avoid this configuration and add @WebFilter(urlPatterns = "/api/*") on the filter and everything will work
 */
@Configuration
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public Filter apiFilter() {
        return new ApiTokenAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").addFilterBefore(apiFilter(), BasicAuthenticationFilter.class);
    }
}
