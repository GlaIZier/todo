package ru.glaizier.todo.security.token;

public class TokenServiceException extends RuntimeException {

    public TokenServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenServiceException(String message) {
        super(message);
    }

}
