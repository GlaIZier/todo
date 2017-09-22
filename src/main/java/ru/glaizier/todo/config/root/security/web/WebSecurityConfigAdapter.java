package ru.glaizier.todo.config.root.security.web;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public abstract class WebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final LogoutHandler apiLogoutHandler;

    public WebSecurityConfigAdapter(
            AuthenticationSuccessHandler authenticationSuccessHandler,
            LogoutHandler apiLogoutHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.apiLogoutHandler = apiLogoutHandler;
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
