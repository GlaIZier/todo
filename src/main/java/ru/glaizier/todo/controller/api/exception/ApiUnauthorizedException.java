package ru.glaizier.todo.controller.api.exception;

public class ApiUnauthorizedException extends ApiControllerException {

    public ApiUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiUnauthorizedException(String message) {
        super(message);
    }
}
