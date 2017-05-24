package ru.glaizier.todo.controller.api.exception;

// Todo import status code here?
public class ApiControllerException extends RuntimeException {

    public ApiControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiControllerException(String message) {
        super(message);
    }

}
