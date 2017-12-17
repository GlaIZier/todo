package ru.glaizier.todo.security.handler;

import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// We extend standard, used by Spring Security authentication handler to add token for api to cookie
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @NonNull
    private final TokenService tokenService;

    @NonNull
    private final PropertiesService propertiesService;


    public LoginSuccessHandler(TokenService tokenService,
                               PropertiesService propertiesService) {
        this.tokenService = tokenService;
        this.propertiesService = propertiesService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication auth) throws IOException, ServletException {
        String token = tokenService.createToken(auth.getName());

        Cookie jwtTokenCookie = new Cookie(propertiesService.getApiTokenCookieName(), token);
        jwtTokenCookie.setPath(propertiesService.getAppEndpointRoot());
        jwtTokenCookie.setMaxAge(propertiesService.getApiTokenExpireDurationInSeconds());
        httpServletResponse.addCookie(jwtTokenCookie);

        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, auth);
    }

}
