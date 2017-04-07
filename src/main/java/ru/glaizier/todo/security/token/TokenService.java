package ru.glaizier.todo.security.token;

import ru.glaizier.todo.domain.User;

public interface TokenService {

    void createToken(User user);

    User verifyToken(String token);

}
