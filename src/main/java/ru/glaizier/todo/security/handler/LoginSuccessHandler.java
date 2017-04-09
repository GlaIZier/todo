package ru.glaizier.todo.security.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
// Todo read about SavedRequestAwareAuthenticationSuccessHandler and other extends
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Autowired
    public LoginSuccessHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            String password = ((UserDetails) principal).getPassword();
            System.out.println("username = " + username);
            System.out.println("password = " + password);
        }
        System.out.println("authentication = " + authentication.getPrincipal());
        System.out.println("authentication = " + authentication.getCredentials());
        Cookie jwtTokenCookie = new Cookie("todo-jwt-token-cookie", "12345");
        jwtTokenCookie.setPath("/todo/");
        jwtTokenCookie.setMaxAge(180);
        httpServletResponse.addCookie(jwtTokenCookie);
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }

}
