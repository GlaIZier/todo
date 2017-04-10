package ru.glaizier.todo.security.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.glaizier.todo.domain.User;

import java.util.Base64;

import javax.annotation.PostConstruct;

@Service
public class JwtTokenService implements TokenService {

    @Value("${api.token.expire.seconds}")
    private int expireInSeconds;

    @Value("${api.token.signing.key}")
    private String signingKey;

    private String base64SigningKey;

    @PostConstruct
    private void init() {
        if (StringUtils.isEmpty(signingKey))
            throw new IllegalArgumentException("Signing key is undefined!");
        base64SigningKey = Base64.getEncoder().encodeToString(signingKey.getBytes());
    }


    @Override
    public String createToken(UserDetails userDetails) {
        return signingKey;
    }

    @Override
    public User verifyToken(String token) {
        return null;
    }
}
