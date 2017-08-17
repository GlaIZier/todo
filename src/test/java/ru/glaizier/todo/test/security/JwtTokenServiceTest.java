package ru.glaizier.todo.test.security;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import ru.glaizier.todo.security.token.JwtTokenService;
import ru.glaizier.todo.security.token.TokenService;

import java.util.Optional;

public class JwtTokenServiceTest {

    private TokenService tokenService = new JwtTokenService(180_000, "secret");

    private String dummyLogin = "dummyLogin";

    @Test
    public void tokenIsValidForUserForWhomItWasCreatedFor() {
        String token = tokenService.createToken(dummyLogin);
        assertThat(tokenService.verifyToken(token), is(Optional.of(dummyLogin)));
    }

    @Test
    public void returnNullWhenTokenIsInvalid() {
        String token = tokenService.createToken(dummyLogin);
        // change first with last char
        char last = token.charAt(token.length() - 1);
        char prevLast = token.charAt(token.length() - 2);
        token = token.substring(0, token.length() - 2);
        token = token + last + prevLast;
        assertThat(tokenService.verifyToken(token), is(Optional.empty()));
    }

    @Test
    public void tokensAreNotTheSameForTheSameLogin() {
        assertNotEquals(tokenService.createToken(dummyLogin), tokenService.createToken(dummyLogin));
    }

    @Test
    public void tokenIsInvalidAfterExpiration() throws InterruptedException {
        JwtTokenService jwtTokenService = new JwtTokenService(1, "secret");
        String token = jwtTokenService.createToken(dummyLogin);
        Thread.sleep(2000);
        assertThat(jwtTokenService.verifyToken(token), is(Optional.empty()));
    }

    @Test
    public void returnNullWhenTokenWasInvalidated() {
        String token = tokenService.createToken(dummyLogin);
        tokenService.invalidateToken(token);
        // change first with last char
        assertThat(tokenService.verifyToken(token), is(Optional.empty()));
    }

}
