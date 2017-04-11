package ru.glaizier.todo.security.token;

public interface TokenService {

    String createToken(String login);

    String verifyToken(String token);

}
