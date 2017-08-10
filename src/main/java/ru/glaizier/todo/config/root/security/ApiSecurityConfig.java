package ru.glaizier.todo.config.root.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.filter.ApiTokenAuthenticationFilter;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.Filter;

/**
 * The order is needed to be before WebSecurityConfig, because WebSecurity config matches all paths, but
 * this config matches only /api/**. So if it is after then nothing is going to happen.
 * Also we can avoid this configuration and add @WebFilter(urlPatterns = "/api/*") on the filter and everything will work
 */
@Configuration
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenService tokenService;

    private PropertiesService propertiesService;

    @Autowired
    public ApiSecurityConfig(TokenService tokenService, PropertiesService propertiesService) {
        this.tokenService = tokenService;
        this.propertiesService = propertiesService;
    }

    @Bean
    public Filter apiFilter() {
        return new ApiTokenAuthenticationFilter(tokenService, propertiesService);
    }

    @Override
    // Todo add here custom csrf protection
    // https://blog.jdriven.com/2014/10/stateless-spring-security-part-1-stateless-csrf-protection/
    // https://en.wikipedia.org/wiki/Same-origin_policy
    // https://stackoverflow.com/questions/20862299/with-spring-security-3-2-0-release-how-can-i-get-the-csrf-token-in-a-page-that
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**/me/**").addFilterBefore(apiFilter(), BasicAuthenticationFilter.class).csrf().disable();
    }
}
