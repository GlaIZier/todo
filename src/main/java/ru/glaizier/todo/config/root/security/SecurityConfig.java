package ru.glaizier.todo.config.root.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.handler.ApiLogoutHandler;
import ru.glaizier.todo.security.handler.LoginSuccessHandler;
import ru.glaizier.todo.security.token.JwtTokenService;
import ru.glaizier.todo.security.token.TokenService;

@EnableWebSecurity
@Import({
        ApiTasksSecurityConfig.class,
        ApiSecurityConfig.class,
        WebSecurityConfig.class,
        MemoryWebSecurityConfig.class
})
public class SecurityConfig {

    private PropertiesService propertiesService;

    @Autowired
    public SecurityConfig(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @Bean
    public TokenService tokenService() {
        return new JwtTokenService(propertiesService.getApiTokenExpireDurationInSeconds(),
                propertiesService.getApiTokenSigningKey());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder(propertiesService.getPasswordEncoderSecret());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginSuccessHandler(tokenService(), propertiesService.getApiTokenCookieName());
    }

    @Bean
    public LogoutHandler apiLogoutHandler() {
        return new ApiLogoutHandler(tokenService(), propertiesService.getApiTokenCookieName());
    }

}
