package ru.glaizier.todo.controller.api.exception;

public class ApiForbiddenException extends ApiControllerException {

    public ApiForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiForbiddenException(String message) {
        super(message);
    }
}
