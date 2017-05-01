package ru.glaizier.todo.controller.api;

public class RestControllerNotFoundException extends RestControllerException {

    public RestControllerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerNotFoundException(String message) {
        super(message);
    }

    public RestControllerNotFoundException() {
        super("");
    }
}
