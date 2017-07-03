package ru.glaizier.todo.persistence.exception;

public class AccessDeniedException extends PersistenceException {
    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
