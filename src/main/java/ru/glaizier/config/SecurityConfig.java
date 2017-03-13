package ru.glaizier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    // TODO add database authentication
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("password").roles("USER").build());
        manager.createUser(User.withUsername("admin").password("password").roles("USER", "ADMIN").build());
        return manager;
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().hasRole("USER")
                    .and()
                    .httpBasic();
        }
    }

    @Configuration
    @Order
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
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
                    .csrf() // todo enable csrf and create logout using POST http method
                    .disable();
        }
    }

    // todo remove after next commit
   /* @Override
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

//                .and()
//                .requiresChannel()
//                .antMatchers("/")
//                .requiresSecure()
                // csrf
                .and()
                .csrf()
                .disable();
    }*/

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("password").roles("USER", "ADMIN");
    }*/
}
