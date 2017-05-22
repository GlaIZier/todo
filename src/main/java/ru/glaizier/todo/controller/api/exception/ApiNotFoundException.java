package ru.glaizier.todo.controller.api.exception;

public class ApiNotFoundException extends ApiControllerException {

    public ApiNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiNotFoundException(String message) {
        super(message);
    }
}
