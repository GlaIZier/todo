package ru.glaizier.todo.config.root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.glaizier.todo.security.filter.ApiTokenAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Value("${api.token.cookie.name}")
    private String tokenCookieName;

    // Todo move here all beans from security package not autowired
    // Autowired bean declared in SecurityConfig above for remember-me function
    @Autowired
    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public Filter apiFilter() {
        return new ApiTokenAuthenticationFilter();
    }


    @Bean
    // TODO add database authentication and create inmemory dummy UserDetailsService for SecurityTest
    // Todo add password encoding (hash + salt)
    public UserDetailsService inMemoryUserDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("u").password("p").roles("USER").build());
        manager.createUser(User.withUsername("a").password("p").roles("USER", "ADMIN").build());
        return manager;
    }


    @Override
    // configure UserDetailsService
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsService());
        auth.eraseCredentials(false);
    }

    @Override
    // Forbid goto login page when logined
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests() // Todo here add authentication Message provider
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
                .successHandler(authenticationSuccessHandler)

                // logout
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                // Session invalidation is called by default. remember-me-cookie is removed by default.
                // If it is added here then set-cookie header appears twice
                .deleteCookies(tokenCookieName)

                // remember me
                .and()
                .rememberMe()
                .key("todo-remember-me-key-random")
                .rememberMeParameter("remember-me")
                .rememberMeCookieName("todo-remember-me-cookie")
                .tokenValiditySeconds(180)
                .userDetailsService(inMemoryUserDetailsService()) // remember me requires explicitly defined UserDetailsService,

                // when ApiWebSecurityConfigurationAdapter and FormWebSecurityConfigurationAdapter don't (still don't know why)
                .and()
                .requiresChannel()
                .antMatchers("/")
                .requiresSecure()

                // csrf
                .and()
                .csrf() // todo enable csrf, add csrf to login and register page markup and create logout using POST http method
                .disable();

        http.addFilterAfter(apiFilter(), BasicAuthenticationFilter.class); // not sure that it is needed to be inserted after BasicAuthenticationFilter

    }
}
