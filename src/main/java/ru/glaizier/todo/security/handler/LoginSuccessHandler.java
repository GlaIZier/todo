package ru.glaizier.todo.security.handler;

import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    // Todo replace hardcoded values with property ones like max age.
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication auth) throws IOException, ServletException {
        String token = tokenService.createToken(auth.getName());

        Cookie jwtTokenCookie = new Cookie(tokenCookieName, token);
        jwtTokenCookie.setPath("/todo");
        jwtTokenCookie.setMaxAge(84600);
        httpServletResponse.addCookie(jwtTokenCookie);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, auth);
    }

}
