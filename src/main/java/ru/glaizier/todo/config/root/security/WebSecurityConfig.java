package ru.glaizier.todo.config.root.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.glaizier.todo.dao.memory.UserDao;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.handler.LoginSuccessHandler;
import ru.glaizier.todo.security.token.TokenService;

@Configuration
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PropertiesService propertiesService;

    private final TokenService tokenService;

    private final UserDao userDao;

    @Autowired
    public WebSecurityConfig(
            TokenService tokenService,
            PropertiesService propertiesService,
            UserDao userDao) {
        this.propertiesService = propertiesService;
        this.tokenService = tokenService;
        this.userDao = userDao;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginSuccessHandler(tokenService, propertiesService.getApiTokenCookieName());
    }

    @Bean
    // TODO add database authentication and create inmemory dummy UserDetailsService for SecurityTest
    // Todo add password encoding (hash + salt)
    public UserDetailsService inMemoryUserDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        userDao.getUsers().forEach(user ->
//            manager.createUser(
//                    User.withUsername(user.getLogin()).password(String.valueOf(user.getPassword()))
//                            // Roles -> (to) Strings -> List of Strings -> array of Strings
//                            .roles(user.getRoles().stream().map(Role::toString).collect(Collectors.toList())
//                                    .toArray(new String[user.getRoles().size()]))
//                            .build())
//        );
        manager.createUser(User.withUsername("u").password("p").roles("USER").build());
        manager.createUser(User.withUsername("a").password("p").roles("USER", "ADMIN").build());
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
    // Todo Forbid goto login page when logined
    // Todo add custom filter after logout to invalidate token
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // Todo here add authentication Message provider
                // secure /tasks
                .antMatchers("/tasks").hasRole("USER")
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
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                // Session invalidation is called by default. remember-me-cookie is removed by default.
                // If it is added here then set-cookie header appears twice
                .deleteCookies(propertiesService.getApiTokenCookieName())

                // remember me
                .and()
                .rememberMe()
                .key("todo-remember-me-key-random")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("todo-remember-me-cookie")
                .tokenValiditySeconds(180)
                .userDetailsService(inMemoryUserDetailsService()) // remember me requires explicitly defined UserDetailsService,
                // when ApiWebSecurityConfigurationAdapter and FormWebSecurityConfigurationAdapter don't (still don't know why)

                // Todo enable this force redirect to http after application will be done
//                .and()
//                .requiresChannel()
//                .antMatchers("/")
//                .requiresSecure()

                // csrf
                .and()
                .csrf() // todo enable csrf, add csrf to login and register page markup and create logout using POST http method
                .disable();
    }
}
