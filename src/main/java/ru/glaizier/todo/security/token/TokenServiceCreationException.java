package ru.glaizier.todo.security.token;

public class TokenServiceCreationException extends RuntimeException {

    public TokenServiceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenServiceCreationException(String message) {
        super(message);
    }

}
