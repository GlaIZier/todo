package ru.glaizier.todo.config;

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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

        // Todo add logout here because after /logout u can access here
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest().hasRole("USER")
                    .and()
                    .httpBasic()
                    .and()
                    .csrf()
                    .disable();
        }
    }

    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

//        @Bean
//        public AuthenticationSuccessHandler successHandler() {
//            SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
//            handler.setUseReferer(true);
//            handler.
//            handler.onAuthenticationSuccess(LoginSuccessHandler::onAuthenticationSuccess);
//            return handler;
//        }

        private UserDetailsService userDetailsService;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        // autowired bean declared in SecurityConfig above for remember-me function
        @Autowired
        public FormLoginWebSecurityConfigurerAdapter(UserDetailsService userDetailsService,
                                                     AuthenticationSuccessHandler authenticationSuccessHandler) {
            this.userDetailsService = userDetailsService;
            this.authenticationSuccessHandler = authenticationSuccessHandler;
        }

        @Override
        // Forbid goto login page when logined
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
                    .successHandler(authenticationSuccessHandler) // Todo add here cookie and redirect to asked url or /
                    // logout

                    .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    // Session invalidation is called by default. remember-me-cookie is removed by default.
                    // If it is added here then set-cookie header appears twice
                    .deleteCookies("todo-jwt-token-cookie")
                    // remember me
                    .and()
                    .rememberMe()
                    .key("todo-remember-me-key-random")
                    .rememberMeParameter("remember-me")
                    .rememberMeCookieName("todo-remember-me-cookie")
                    .tokenValiditySeconds(180)
                    .userDetailsService(userDetailsService) // remember me requires explicitly defined UserDetailsService,
                    // when ApiWebSecurityConfigurationAdapter and FormWebSecurityConfigurationAdapter don't (still don't know why)
                    .and()
                    .requiresChannel()
                    .antMatchers("/")
                    .requiresSecure()
                    // csrf
                    .and()
                    .csrf() // todo enable csrf, add csrf to login and register page markup and create logout using POST http method
                    .disable();
        }
    }
}
