package ru.glaizier.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class SuccessLoginHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Cookie jwtTokenCookie = new Cookie("todo-jwt-token-cookie", "12345");
        jwtTokenCookie.setDomain("/todo/");
        jwtTokenCookie.setPath("/api/");
        jwtTokenCookie.setMaxAge(180);
        httpServletResponse.addCookie(jwtTokenCookie);
//        httpServletResponse.sendRedirect("/");
    }

}
