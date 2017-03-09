package ru.glaizier.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // secure /tasks
                .antMatchers("/tasks").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/tasks").hasRole("USER")
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
                // remember me
                .and()
                .rememberMe()
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
                .csrf() // todo enable csrf and create post logout
                .disable();
    }

    // TODO add database authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("USER", "ADMIN");
    }
}
