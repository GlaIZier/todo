package ru.glaizier.todo.controller.api.task;

public class RestControllerException extends RuntimeException {

    public RestControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerException(String message) {
        super(message);
    }

}
