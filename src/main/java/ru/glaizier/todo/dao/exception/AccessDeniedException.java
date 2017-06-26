package ru.glaizier.todo.dao.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
