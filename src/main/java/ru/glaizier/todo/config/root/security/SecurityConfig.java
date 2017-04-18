package ru.glaizier.todo.config.root.security;

import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
//@Configuration
@Import({
        WebSecurityConfig.class,
        ApiSecurityConfig.class
})
public class SecurityConfig {

}
