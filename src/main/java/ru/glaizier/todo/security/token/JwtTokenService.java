package ru.glaizier.todo.security.token;

import org.springframework.stereotype.Service;
import ru.glaizier.todo.domain.User;

@Service
public class JwtTokenService implements TokenService {

    @Override
    public void createToken(User user) {

    }

    @Override
    public User verifyToken(String token) {
        return null;
    }
}
