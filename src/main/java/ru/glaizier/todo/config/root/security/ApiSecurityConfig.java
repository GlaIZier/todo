package ru.glaizier.todo.config.root.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.glaizier.todo.security.filter.ApiTokenAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
// Todo Start here describe why it wasn't work!!!Don't know why but if Order(2) here (after WebSecurityConfig) then
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public Filter apiFilter() {
        return new ApiTokenAuthenticationFilter();
    }

    //Todo add custom filter after logout to invalidate token
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").addFilterBefore(apiFilter(), BasicAuthenticationFilter.class); // Todo check after
        // what filter add my not sure that it is needed to be inserted after BasicAuthenticationFilter
    }
}
