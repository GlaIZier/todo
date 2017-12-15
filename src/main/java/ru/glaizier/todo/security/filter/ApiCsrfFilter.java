package ru.glaizier.todo.security.filter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.glaizier.todo.model.dto.api.HttpResponse;
import ru.glaizier.todo.model.dto.api.output.OutputError;
import ru.glaizier.todo.properties.PropertiesService;
import static ru.glaizier.todo.log.MdcConstants.TOKEN;

/**
 * Important!
 * This is not necessary. I could send a header for authentication, not cookie. Hence, in this situation there would be
 * no CSRF attack at all. But I realized it late, when this functionality had been already done. So, I preserve Api
 * authentication with cookie for now. Maybe later I will remove it and replace with header authentication. Anyway,
 * I understand more about Filter chain in Spring Security because of this.
 */
public class ApiCsrfFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();

    private final PropertiesService propertiesService;

    public ApiCsrfFilter(PropertiesService propertiesService) {
        this.propertiesService = propertiesService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (requireCsrfProtectionMatcher.matches(request)) {
            final String csrfTokenValue = request.getHeader(propertiesService.getApiTokenHeaderName());
            final Cookie[] cookies = request.getCookies();

            String csrfCookieValue = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(propertiesService.getApiTokenCookieName())) {
                        csrfCookieValue = cookie.getValue();
                    }
                }
            }

            if (csrfTokenValue == null || !csrfTokenValue.equals(csrfCookieValue)) {
                writeErrorToResponse(csrfTokenValue, response, HttpStatus.FORBIDDEN,
                    new OutputError(
                        new HttpResponse(HttpStatus.FORBIDDEN.value(), "Missing or non-matching CSRF-token!")));
                return;
            }

            try {
                MDC.put(TOKEN, csrfTokenValue);
                log.debug("Api csrf check success!");
            } finally {
                MDC.clear();
            }
        }
        filterChain.doFilter(request, response);
    }

    public static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

        @Override
        public boolean matches(HttpServletRequest request) {
            return !allowedMethods.matcher(request.getMethod()).matches();
        }
    }

    private void writeErrorToResponse(String headerToken, HttpServletResponse resp,
                                      HttpStatus httpStatus,
                                      OutputError error) throws IOException {
        try {
            MDC.put(TOKEN, headerToken);
            log.error("Api csrf check failed with HTTP status {}!", httpStatus);

            ObjectMapper mapper = new ObjectMapper();
            resp.setStatus(httpStatus.value());
            resp.getWriter().write(mapper.writeValueAsString(error));
        } finally {
            MDC.clear();
        }

    }
}

