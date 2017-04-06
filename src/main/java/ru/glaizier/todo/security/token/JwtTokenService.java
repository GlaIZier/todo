package ru.glaizier.todo.security.token;

import org.springframework.stereotype.Service;

@Service
public class JwtTokenService implements TokenService {

    @Override
    public void createToken(String login, String password) {

    }
}
