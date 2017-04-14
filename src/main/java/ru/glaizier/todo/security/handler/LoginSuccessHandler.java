package ru.glaizier.todo.security.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.security.token.TokenService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
// Todo read about SavedRequestAwareAuthenticationSuccessHandler and other extends
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenService tokenService;

    private final String tokenCookieName;

    @Autowired
    public LoginSuccessHandler(TokenService tokenService,
                               @Value("${api.token.cookie.name}") String tokenCookieName) {
        this.tokenService = tokenService;
        this.tokenCookieName = tokenCookieName;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication auth) throws IOException, ServletException {
        String token = tokenService.createToken(auth.getName());

        Cookie jwtTokenCookie = new Cookie(tokenCookieName, token);
        jwtTokenCookie.setPath("/todo");
        jwtTokenCookie.setMaxAge(180);
        httpServletResponse.addCookie(jwtTokenCookie);

        //Todo Start here. Read about filters and create filter to api
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, auth);
    }

}
