package ru.glaizier.todo.config.root.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.persistence.Persistence;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.handler.ApiLogoutHandler;
import ru.glaizier.todo.security.handler.LoginSuccessHandler;
import ru.glaizier.todo.security.token.TokenService;

import java.util.stream.Collectors;

@Configuration
@Order(3)
@Profile("memory")
// Todo add hierarchy
// Todo add update userDetailsManager when saveUser, updateUser and deleteUser
public class MemoryWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PropertiesService propertiesService;

    private final TokenService tokenService;

    private final Persistence persistence;

    @Autowired
    public MemoryWebSecurityConfig(
            TokenService tokenService,
            PropertiesService propertiesService,
            Persistence persistence) {
        this.propertiesService = propertiesService;
        this.tokenService = tokenService;
        this.persistence = persistence;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginSuccessHandler(tokenService, propertiesService.getApiTokenCookieName());
    }

    @Bean
    public LogoutHandler apiLogoutHandler() {
        return new ApiLogoutHandler(tokenService, propertiesService.getApiTokenCookieName());
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("u").password("p").roles("USER").build());
//        manager.createUser(User.withUsername("a").password("p").roles("USER", "ADMIN").build());
        persistence.findUsers().forEach(user ->
                manager.createUser(
                        User.withUsername(user.getLogin()).password(String.valueOf(user.getPassword()))
                                // Roles -> (to) Strings -> Remove ROLE_ from strings -> List of Strings -> array of Strings
                                .roles(user.getRoles().orElseThrow(IllegalStateException::new).stream()
                                        .map(RoleDto::getRole)
                                        .map(r -> r.replaceFirst("ROLE_", ""))
                                        .collect(Collectors.toList())
                                        .toArray(new String[user.getRoles().orElseThrow(IllegalStateException::new).size()]))
                                .build())
        );
        return manager;
    }

    @Override
    // configure UserDetailsService
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsService());
        // Don't erase password after authentication
        auth.eraseCredentials(false);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // secure /tasks
                .antMatchers("/tasks/**").hasRole("USER")
                .anyRequest().permitAll()

                // login
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("user")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())

                // logout
                .and()
                .logout()
                .logoutUrl("/logout")
                // make this to enable get logout because by default it's forbidden to use get with csrf protection
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                // Session invalidation is called by default. remember-me-cookie is removed by default.
                // If it is added here then set-cookie header appears twice
//                .deleteCookies(propertiesService.getApiTokenCookieName())
                .addLogoutHandler(apiLogoutHandler())

                // Default in-memory hash implementation. This doesn't save remember-me cookie if server restarts.
                // To enable saving between restarts table persistent_logins in db needs to be created and it will be
                // better of course because we can provide some logic to invalidate tokens etc.
                // http://jaspan.com/improved_persistent_login_cookie_best_practice
                .and()
                .rememberMe()
                .key("todo-remember-me-key-random")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("todo-remember-me-cookie")
                .tokenValiditySeconds(60 * 60 * 24)

                // Todo enable this force redirect to http after application will be done
//                .and()
//                .requiresChannel()
//                .antMatchers("/")
//                .requiresSecure()

                // csrf
                .and()
                .csrf() // csrf is automatically added by Thymeleaf(?) to each post form like login and register
        ;
    }
}
