package ru.glaizier.todo.controller.api.task;

public class RestControllerBadRequestException extends RestControllerException {

    public RestControllerBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerBadRequestException(String message) {
        super(message);
    }
}
