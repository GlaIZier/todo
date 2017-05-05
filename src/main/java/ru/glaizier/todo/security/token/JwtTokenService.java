package ru.glaizier.todo.security.token;

import static java.lang.String.format;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import ru.glaizier.todo.log.MdcConstants;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtTokenService implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String TODO_API_TOKEN_JWT_ISSUER = "todo-api-token-jwt-issuer";

    private final int expireDurationInSeconds;

    @NonNull
    private final Algorithm algorithm;

    public JwtTokenService(int expireDurationInSeconds, @NonNull String signingKey) {
        if (expireDurationInSeconds <= 0)
            throw new TokenServiceCreationException(format("Expire duration in seconds have the wrong value: %d!",
                    expireDurationInSeconds));

        this.expireDurationInSeconds = expireDurationInSeconds;
        try {
            // Todo inject this when Security will be finished
            this.algorithm = Algorithm.HMAC512(signingKey);
        } catch (UnsupportedEncodingException e) {
            throw new TokenServiceCreationException("Couldn't create algorithm to sign api tokens!", e);
        }
    }

    private Date getExpirationDate(Date from) {
        return new Date(from.getTime() + TimeUnit.SECONDS.toMillis(expireDurationInSeconds));
    }

    @Override
    public String createToken(@NonNull String login) {
        Date now = new Date();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString()) // different tokens for the same login in different times
                .withIssuer(TODO_API_TOKEN_JWT_ISSUER)
                .withSubject(login)
                .withIssuedAt(now)
                .withExpiresAt(getExpirationDate(now))
                .sign(algorithm);
    }

    @Override
    public Optional<String> verifyToken(String token) throws TokenDecodingException {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(TODO_API_TOKEN_JWT_ISSUER)
                .build();
        try {
            DecodedJWT verify = verifier.verify(token);
            return Optional.of(verify.getSubject());
        } catch (JWTDecodeException e) {
            throw new TokenDecodingException("Token decoding failed!", e);
        } catch (JWTVerificationException e) {
            try {
                MDC.put(MdcConstants.TOKEN, token);
                log.error("Token verification failed! {}", e.getMessage());
            } finally {
                MDC.clear();
            }
            return Optional.empty();
        }
    }
}
