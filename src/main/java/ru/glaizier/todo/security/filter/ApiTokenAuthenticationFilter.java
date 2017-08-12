package ru.glaizier.todo.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import ru.glaizier.todo.model.dto.api.output.OutputError;
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
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import static ru.glaizier.todo.log.MdcConstants.LOGIN;
import static ru.glaizier.todo.log.MdcConstants.TOKEN;

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

        Optional<String> token = findToken(req);
        if (!token.isPresent()) {
            writeErrorToResponse(null, resp, HttpStatus.UNAUTHORIZED, OutputError.UNAUTHORIZED);
            return;
        }
        try {
            Optional<String> login = tokenService.verifyToken(token.get());
            if (login.isPresent()) {
                req.getSession().setAttribute(propertiesService.getApiTokenSessionAttributeName(), login.get());
                try {
                    MDC.put(LOGIN, login.get());
                    MDC.put(TOKEN, token.get());
                    log.debug("Api authentication success!");
                } finally {
                    MDC.clear();
                }
            }
            else {
                writeErrorToResponse(token.get(),
                        resp, HttpStatus.UNAUTHORIZED, OutputError.UNAUTHORIZED);
                return;
            }

        } catch (TokenDecodingException e) {
            log.error("Api authentication token decoding failed: " + e.getMessage(), e);
            writeErrorToResponse(token.get(),
                    resp, HttpStatus.BAD_REQUEST, OutputError.BAD_REQUEST);
            return;
        }

        chain.doFilter(request, response);
    }

    private Optional<String> findToken(HttpServletRequest req) {
        Optional<Cookie> tokenCookie = findTokenCookie(req);
        if (tokenCookie.isPresent())
            return Optional.of(tokenCookie.get().getValue());

        return findTokenHeader(req);
    }

    private Optional<String> findTokenHeader(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(propertiesService.getApiTokenHeaderName()));
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
                                      OutputError error) throws IOException {
        try {
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