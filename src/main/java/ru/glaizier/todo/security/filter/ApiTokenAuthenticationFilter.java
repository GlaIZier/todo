package ru.glaizier.todo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.GenericFilterBean;
import ru.glaizier.todo.domain.api.ApiError;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenService;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Todo add tests for api filtering process
public class ApiTokenAuthenticationFilter extends GenericFilterBean {

    private final PropertiesService propertiesService;

    private final TokenService tokenService;

    public ApiTokenAuthenticationFilter(TokenService tokenService, PropertiesService propertiesService) {
        this.tokenService = tokenService;
        this.propertiesService = propertiesService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;

        Optional<Cookie> optionalTokenCookie = findTokenCookie(req);
        if (!findTokenCookie(req).isPresent() ||
                tokenService.verifyToken(optionalTokenCookie.get().getValue()) == null) {
            writeUnauthorizedErrorToResponse(resp);
            return;
        }

        chain.doFilter(request, response);
    }

    private Optional<Cookie> findTokenCookie(HttpServletRequest req) {
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(propertiesService.getApiTokenCookieName())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    private void writeUnauthorizedErrorToResponse(HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.getWriter().write(mapper.writeValueAsString(ApiError.UNAUTHORIZED));
    }
}