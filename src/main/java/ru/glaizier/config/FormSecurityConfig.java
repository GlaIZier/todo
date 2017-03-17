package ru.glaizier.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// Todo remove after tests
@Configuration
@Order(2)
public class FormSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // secure /tasks
                .antMatchers("/tasks").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/tasks").hasRole("USER") // todo remove it?
                .anyRequest().permitAll()
                // login
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("login")
                .passwordParameter("password")
                // logout
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
//                    .deleteCookies("todo-remember-me-cookie")
                // remember me
                .and()
                .rememberMe()
//                .userDetailsService()
                .key("todo-remember-me-key").
                rememberMeParameter("remember-me").
                rememberMeCookieName("todo-remember-me-cookie").
                tokenValiditySeconds(2419200)
                // todo https for all web app. It will need to configure tomcat to resolve 8443 port. http://www.baeldung.com/spring-channel-security-https
//                .and()
//                .requiresChannel()
//                .antMatchers("/")
//                .requiresSecure()
                // csrf
                .and()
                .csrf() // todo enable csrf and create logout using POST http method
                .disable();
    }

}
