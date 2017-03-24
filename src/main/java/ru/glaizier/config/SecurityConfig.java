package ru.glaizier.config;

import org.springframework.beans.factory.annotation.Autowired;
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
    // TODO add database authentication and create inmemory dummy UserDetailsService for SecurityTest
    // Todo add password encoding (hash + salt)
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("u").password("p").roles("USER").build());
        manager.createUser(User.withUsername("a").password("p").roles("USER", "ADMIN").build());
        return manager;
    }

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().hasRole("USER")
                    .and()
                    .httpBasic();
        }
    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private UserDetailsService userDetailsService;

        // autowired bean declared in SecurityConfig above for remember-me function
        @Autowired
        public FormLoginWebSecurityConfigurerAdapter(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

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
                    .usernameParameter("user")
                    .passwordParameter("password")
                    // logout
                    // Session invalidation and remember-me-cookie are cleaned by default
                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    // remember me
                    .and()
                    .rememberMe()
                    .key("remember-me-key-random")
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("remember-me-cookie")
                    .tokenValiditySeconds(2419200)
                    .userDetailsService(userDetailsService) // remember me requires explicitly defined UserDetailsService,
                    // when ApiWebSecurityConfigurationAdapter and FormWebSecurityConfigurationAdapter don't (still don't know why)
                    // todo https for all web app. It will need to configure tomcat to resolve 8443 port. http://www.baeldung.com/spring-channel-security-https
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
}
