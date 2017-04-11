package ru.glaizier.todo.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

@Service
public class JwtTokenService implements TokenService {

    private static final String TODO_API_TOKEN_JWT_ISSUER = "todo-api-token-jwt-issuer";

    private int expireInSecondsDuration;

    private String signingKey;

    private Algorithm algorithm;

    public JwtTokenService(@Value("${api.token.expire.seconds}") int expireInSecondsDuration,
                           @Value("${api.token.signing.key}") String signingKey,
                           Algorithm algorithm) {
        this.expireInSecondsDuration = expireInSecondsDuration;
        this.signingKey = signingKey;
        this.algorithm = algorithm;
    }

    @PostConstruct
    private void init() {
        if (StringUtils.isEmpty(signingKey))
            throw new IllegalArgumentException("Signing key is undefined!");
        try {
            algorithm = Algorithm.HMAC512(signingKey);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Couldn't create algorithm to sign api tokens!", e);
        }
    }

    private Date getExpirationDate(Date from) {
        return new Date(from.getTime() + TimeUnit.SECONDS.toMillis(expireInSecondsDuration));
    }

    @Override
    public String createToken(String login) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(TODO_API_TOKEN_JWT_ISSUER)
                .withSubject(login)
                .withIssuedAt(now)
                .withExpiresAt(getExpirationDate(now))
                .sign(algorithm);

    }

    @Override
    public String verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(TODO_API_TOKEN_JWT_ISSUER)
                .build();
        DecodedJWT verify = verifier.verify(token);
        return verify.getToken();
    }
}
