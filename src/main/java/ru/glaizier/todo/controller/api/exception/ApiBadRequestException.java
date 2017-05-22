package ru.glaizier.todo.controller.api.exception;

public class ApiBadRequestException extends ApiControllerException {

    public ApiBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiBadRequestException(String message) {
        super(message);
    }
}
