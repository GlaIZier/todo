package ru.glaizier.todo.security.handler;

import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;
import ru.glaizier.todo.security.token.TokenService;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiLogoutHandler implements LogoutHandler {

    @NonNull
    private final TokenService tokenService;

    @NonNull
    private final String tokenCookieName;

    public ApiLogoutHandler(TokenService tokenService,
                            String tokenCookieName) {
        this.tokenService = tokenService;
        this.tokenCookieName = tokenCookieName;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        findCookieToken(request).ifPresent(tokenService::invalidateToken);
        clearApiTokenCookie(request, response);
    }

    private void clearApiTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenCookieName, null);
        String cookiePath = request.getContextPath();
        if (!StringUtils.hasLength(cookiePath)) {
            cookiePath = "/";
        }
        cookie.setPath(cookiePath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private Optional<String> findCookieToken(HttpServletRequest req) {
        if (req.getCookies() == null)
            return Optional.empty();
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(tokenCookieName)) {
                return Optional.of(c.getValue());
            }
        }
        return Optional.empty();
    }


}
