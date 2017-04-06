package ru.glaizier.todo.security.token;

public interface TokenService {

    void createToken(String login, String password);

}
