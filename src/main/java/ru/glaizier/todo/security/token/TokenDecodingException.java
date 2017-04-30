package ru.glaizier.todo.security.token;

public class TokenDecodingException extends RuntimeException {

    public TokenDecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenDecodingException(String message) {
        super(message);
    }

}
