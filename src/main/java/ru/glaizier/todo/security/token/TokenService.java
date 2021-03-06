package ru.glaizier.todo.security.token;

import java.util.Optional;

public interface TokenService {

    /**
     * @param login login to create token for
     * @return created token
     */
    String createToken(String login);

    /**
     * @param token token to check
     * @return login for whom token has been created or Optional.empty() if verification has been failed
     */
    Optional<String> verifyToken(String token) throws TokenDecodingException;

    void invalidateToken(String token);

    void cleanUpInvalidatedTokens();
}
