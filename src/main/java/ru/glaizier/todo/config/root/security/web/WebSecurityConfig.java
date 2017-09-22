package ru.glaizier.todo.config.root.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.sql.DataSource;

@Configuration
@Order(3)
@Profile({"default", "prod"})
public class WebSecurityConfig extends WebSecurityConfigAdapter {

    private static final String USER_BY_LOGIN_QUERY = "select login, password, true as enabled from todo.User where login=?";
    private static final String AUTHORITY_BY_LOGIN_QUERY = "select login, role from todo.Authorization where login=?";

    private final DataSource dataSource;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(
            AuthenticationSuccessHandler authenticationSuccessHandler,
            LogoutHandler apiLogoutHandler,
            DataSource dataSource,
            PasswordEncoder passwordEncoder) {
        super(authenticationSuccessHandler, apiLogoutHandler);
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
}
