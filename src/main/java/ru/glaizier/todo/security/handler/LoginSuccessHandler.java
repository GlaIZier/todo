package ru.glaizier.todo.security.handler;

import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import ru.glaizier.todo.security.token.TokenService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Todo read about SavedRequestAwareAuthenticationSuccessHandler and other extends
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @NonNull
    private final TokenService tokenService;

    @NonNull
    private final String tokenCookieName;

    public LoginSuccessHandler(TokenService tokenService,
                               String tokenCookieName) {
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

        //Todo Read about filters and create filter to api
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, auth);
    }

}
