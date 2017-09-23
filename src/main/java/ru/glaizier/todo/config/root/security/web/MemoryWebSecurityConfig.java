package ru.glaizier.todo.config.root.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@Order(3)
@Profile("memory")
public class MemoryWebSecurityConfig extends WebSecurityConfigAdapter {

    @Autowired
    public MemoryWebSecurityConfig(
            AuthenticationSuccessHandler authenticationSuccessHandler,
            LogoutHandler apiLogoutHandler) {
        super(authenticationSuccessHandler, apiLogoutHandler);
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsService() throws Exception {
        return new InMemoryUserDetailsManager();
    }

    @Override
    // configure UserDetailsService
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsService());
        // Don't erase password after authentication
        auth.eraseCredentials(false);
    }
}
