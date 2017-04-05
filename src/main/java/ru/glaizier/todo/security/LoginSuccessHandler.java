package ru.glaizier.todo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
// Todo read about SavedRequestAwareAuthenticationSuccessHandler and other extends
// Todo move this bean to SecurityConfig with @Bean
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Cookie jwtTokenCookie = new Cookie("todo-jwt-token-cookie", "12345");
        // Todo start here and add url relative
        jwtTokenCookie.setPath("/todo/");
        jwtTokenCookie.setMaxAge(180);
        httpServletResponse.addCookie(jwtTokenCookie);
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }

}
