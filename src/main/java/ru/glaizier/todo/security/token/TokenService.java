package ru.glaizier.todo.security.token;

import org.springframework.security.core.userdetails.UserDetails;
import ru.glaizier.todo.domain.User;

public interface TokenService {

    String createToken(UserDetails user);

    User verifyToken(String token);

}
