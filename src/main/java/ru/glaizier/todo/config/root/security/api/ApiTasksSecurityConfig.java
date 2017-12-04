package ru.glaizier.todo.config.root.security.api;


import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.filter.ApiCsrfFilter;
import ru.glaizier.todo.security.filter.ApiTokenAuthenticationFilter;
import ru.glaizier.todo.security.token.TokenService;

/**
 * The order is needed to be before WebSecurityConfig, because WebSecurity config matches all paths, but
 * this config matches only /api/**\/me/**. So if it is after then nothing is going to happen.
 * Also we can avoid filters specific configuration and add @WebFilter(urlPatterns = "/api/**\/me/**") on the filter
 * and everything will work
 */
@Configuration
@Order(1)
public class ApiTasksSecurityConfig extends ApiSecurityConfigAdapter {

    private TokenService tokenService;

    private PropertiesService propertiesService;

    @Autowired
    public ApiTasksSecurityConfig(TokenService tokenService, PropertiesService propertiesService) {
        this.tokenService = tokenService;
        this.propertiesService = propertiesService;
    }

    @Bean
    public Filter apiFilter() {
        return new ApiTokenAuthenticationFilter(tokenService, propertiesService);
    }

    @Bean
    public Filter apiCsrfFilter() {
        return new ApiCsrfFilter(propertiesService);
    }

    @Override
    // These api urls are protected from csrf and unauthorized access
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/api/**/me/**")
            .cors().and()
            .addFilterBefore(apiFilter(), BasicAuthenticationFilter.class)
            .csrf().disable().addFilterBefore(apiCsrfFilter(), CsrfFilter.class);
    }
}
