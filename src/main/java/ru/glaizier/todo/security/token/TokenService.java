package ru.glaizier.todo.security.token;

public interface TokenService {

    /**
     * @param login login to create token for
     * @return created token
     */
    String createToken(String login);

    /**
     * @param token token to check
     * @return login for whom token has been created or null if verification has been failed
     */
    String verifyToken(String token);

}
