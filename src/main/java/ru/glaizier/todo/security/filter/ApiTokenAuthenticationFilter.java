package ru.glaizier.todo.security.filter;

import static ru.glaizier.todo.log.MdcConstants.LOGIN;
import static ru.glaizier.todo.log.MdcConstants.TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import ru.glaizier.todo.domain.api.ApiError;
import ru.glaizier.todo.properties.PropertiesService;
import ru.glaizier.todo.security.token.TokenDecodingException;
import ru.glaizier.todo.security.token.TokenService;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiTokenAuthenticationFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
            writeErrorToResponse(null, resp, HttpStatus.UNAUTHORIZED, ApiError.UNAUTHORIZED);
            return;
        }
        try {
            Optional<String> login = tokenService.verifyToken(optionalTokenCookie.get().getValue());
            if (login.isPresent()) {
                req.getSession().setAttribute(propertiesService.getApiTokenSessionAttributeName(), login.get());
                // Todo move to aspect
                try {
                    MDC.put(LOGIN, login.get());
                    MDC.put(TOKEN, optionalTokenCookie.get().getValue());
                    log.debug("Api authentication success!");
                } finally {
                    MDC.clear();
                }
            }
            else {
                writeErrorToResponse(optionalTokenCookie.get().getValue(),
                        resp, HttpStatus.UNAUTHORIZED, ApiError.UNAUTHORIZED);
                return;
            }

        } catch (TokenDecodingException e) {
            log.error("Api authentication token decoding failed: " + e.getMessage(), e);
            writeErrorToResponse(optionalTokenCookie.get().getValue(),
                    resp, HttpStatus.BAD_REQUEST, ApiError.BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }

    private Optional<Cookie> findTokenCookie(HttpServletRequest req) {
        if (req.getCookies() == null)
            return Optional.empty();
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(propertiesService.getApiTokenCookieName())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    private void writeErrorToResponse(String token, HttpServletResponse resp,
                                      HttpStatus httpStatus,
                                      ApiError error) throws IOException {
        try {
            // Todo move to aspect
            MDC.put(TOKEN, token);
            log.error("Api authentication failed with HTTP status {}!", httpStatus);

            ObjectMapper mapper = new ObjectMapper();
            resp.setStatus(httpStatus.value());
            resp.getWriter().write(mapper.writeValueAsString(error));
        } finally {
            MDC.clear();
        }

    }
}