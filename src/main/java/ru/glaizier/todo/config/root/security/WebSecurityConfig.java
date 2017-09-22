package ru.glaizier.todo.config.root.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@Order(3)
@Profile({"default", "prod"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USER_BY_LOGIN_QUERY = "select login, password, true as enabled from todo.User where login=?";
    private static final String AUTHORITY_BY_LOGIN_QUERY = "select login, role from todo.Authorization where login=?";

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final LogoutHandler apiLogoutHandler;

    private final DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(
            AuthenticationSuccessHandler authenticationSuccessHandler,
            LogoutHandler apiLogoutHandler,
            DataSource dataSource,
            PasswordEncoder passwordEncoder) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.apiLogoutHandler = apiLogoutHandler;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    // configure UserDetailsService
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USER_BY_LOGIN_QUERY)
                .authoritiesByUsernameQuery(AUTHORITY_BY_LOGIN_QUERY)
                .passwordEncoder(passwordEncoder);
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
                .successHandler(authenticationSuccessHandler)

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
                .addLogoutHandler(apiLogoutHandler)

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
