package ru.glaizier.todo.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import lombok.NonNull;
import ru.glaizier.todo.security.token.TokenService;

// Todo read about SavedRequestAwareAuthenticationSuccessHandler and other extends
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @NonNull
    private final TokenService tokenService;

    @NonNull
    private final String tokenCookieName;

    private final int tokenCookieMaxAge;

    public LoginSuccessHandler(TokenService tokenService,
                               String tokenCookieName,
                               int tokenCookieMaxAge) {
        this.tokenService = tokenService;
        this.tokenCookieName = tokenCookieName;
        this.tokenCookieMaxAge = tokenCookieMaxAge;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication auth) throws IOException, ServletException {
        String token = tokenService.createToken(auth.getName());

        Cookie jwtTokenCookie = new Cookie(tokenCookieName, token);
        jwtTokenCookie.setPath("/todo");
        jwtTokenCookie.setMaxAge(tokenCookieMaxAge);
        httpServletResponse.addCookie(jwtTokenCookie);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, auth);
    }

}
