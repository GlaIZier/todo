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

/**
 * Is used for authentication in traditional web application
 */
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
//        manager.createUser(User.withUsername("u").password("p").roles("USER").build());
//        manager.createUser(User.withUsername("a").password("p").roles("USER", "ADMIN").build());
//        persistence.findUsers().forEach(user ->
//                userDetailsManager.createUser(
//                        User.withUsername(user.getLogin()).password(String.valueOf(user.getPassword()))
//                                // Roles -> (to) Strings -> Remove ROLE_ from strings -> List of Strings -> array of Strings
//                                .roles(user.getRoles().orElseThrow(IllegalStateException::new).stream()
//                                        .map(RoleDto::getRole)
//                                        .map(r -> r.replaceFirst("ROLE_", ""))
//                                        .collect(Collectors.toList())
//                                        .toArray(new String[user.getRoles().orElseThrow(IllegalStateException::new).size()]))
//                                .build())
//        );
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
