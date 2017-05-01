package ru.glaizier.todo.controller.api;

public class RestControllerException extends RuntimeException {

    public RestControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerException(String message) {
        super(message);
    }

}
