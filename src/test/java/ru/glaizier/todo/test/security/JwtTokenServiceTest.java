package ru.glaizier.todo.test.security;

import static org.junit.Assert.*;

import org.junit.Test;
import ru.glaizier.todo.security.token.JwtTokenService;

public class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService = new JwtTokenService(180_000, "secret");

    private String dummyLogin = "dummyLogin";

    @Test
    public void tokenIsValidForUserForWhomItWasCreatedFor() {
        String token = jwtTokenService.createToken(dummyLogin);
        assertEquals(dummyLogin, jwtTokenService.verifyToken(token));
    }

    @Test
    public void returnNullWhenTokenIsInvalid() {
        String token = jwtTokenService.createToken(dummyLogin);
        // change first with last char
        char last = token.charAt(token.length() - 1);
        char prevLast = token.charAt(token.length() - 2);
        token = token.substring(0, token.length() - 2);
        token = token + last + prevLast;
        assertNull(jwtTokenService.verifyToken(token));
    }

    @Test
    public void tokensAreNotTheSameForTheSameLogin() {
        assertNotEquals(jwtTokenService.createToken(dummyLogin), jwtTokenService.createToken(dummyLogin));
    }

    @Test
    public void tokenIsInvalidAfterExpiration() throws InterruptedException {
        JwtTokenService jwtTokenService = new JwtTokenService(1, "secret");
        String token = jwtTokenService.createToken(dummyLogin);
        Thread.sleep(2000);
        assertNull(jwtTokenService.verifyToken(token));
    }

}
