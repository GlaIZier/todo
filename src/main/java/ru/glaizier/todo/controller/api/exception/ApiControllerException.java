package ru.glaizier.todo.controller.api.exception;

public class ApiControllerException extends RuntimeException {

    public ApiControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiControllerException(String message) {
        super(message);
    }

}
