package ru.glaizier.todo.security.filter;

import org.springframework.web.filter.GenericFilterBean;
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
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Cookie tokenCookie = optionalTokenCookie.get();
        System.out.println("tokenCookie.getValue() = " + tokenCookie.getValue());
        chain.doFilter(request, response);

//        findTokenCookie(req).ifPresent((tokenCookie) -> {
//            System.out.println("tokenCookie.getValue() = " + tokenCookie.getValue());
//            chain.doFilter(request, response);
//        });


//        System.out.println("tokenCookie.getName() = " + tokenCookie..getName());
        //        final String authHeaderVal = req.getHeader(authHeader);
//
//        if (null==authHeaderVal)
//        {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        try
//        {
//            JwtUser jwtUser = jwtTokenService.getUser(authHeaderVal);
//            req.setAttribute("jwtUser", jwtUser);
//        }
//        catch(JwtException e)
//        {
//            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//            return;
//        }
//
//        chain.doFilter(req, resp);
    }

    private Optional<Cookie> findTokenCookie(HttpServletRequest req) {
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(propertiesService.getApiTokenCookieName())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}