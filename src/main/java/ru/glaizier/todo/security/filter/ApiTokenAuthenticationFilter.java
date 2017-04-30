package ru.glaizier.todo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import ru.glaizier.todo.domain.api.ApiError;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenDecodingException;
import ru.glaizier.todo.security.token.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

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
        if (!findTokenCookie(req).isPresent()) {
            writeErrorToResponse(resp, HttpStatus.UNAUTHORIZED, ApiError.UNAUTHORIZED);
            return;
        }
        String login;
        try {
            login = tokenService.verifyToken(optionalTokenCookie.get().getValue());

        } catch (TokenDecodingException e) {
            // Todo add logging
            e.printStackTrace();
            writeErrorToResponse(resp, HttpStatus.BAD_REQUEST, ApiError.BAD_REQUEST);
            return;
        }

        if (login != null) {
            // Todo get session and add here info
        } else {
            writeErrorToResponse(resp, HttpStatus.UNAUTHORIZED, ApiError.UNAUTHORIZED);
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

    private void writeErrorToResponse(HttpServletResponse resp,
                                      HttpStatus httpStatus,
                                      ApiError error) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setStatus(httpStatus.value());
        resp.getWriter().write(mapper.writeValueAsString(error));
    }
}