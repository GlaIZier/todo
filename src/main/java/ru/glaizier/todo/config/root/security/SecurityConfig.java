package ru.glaizier.todo.config.root.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.JwtTokenService;
import ru.glaizier.todo.security.token.TokenService;

@EnableWebSecurity
@Import({
        WebSecurityConfig.class,
        ApiSecurityConfig.class
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

}
