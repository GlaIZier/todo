package ru.glaizier.todo.controller.api.task;

public class RestControllerBadRequesException extends RestControllerException {

    public RestControllerBadRequesException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerBadRequesException(String message) {
        super(message);
    }
}
