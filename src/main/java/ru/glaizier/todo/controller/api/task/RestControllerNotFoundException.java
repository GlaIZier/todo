package ru.glaizier.todo.controller.api.task;

import static java.lang.String.format;

public class RestControllerNotFoundException extends RestControllerException {

    public final static String TASK_NOT_FOUND_FORMAT_MESSAGE = "Task for user %s with id %d hasn't been found!";

    public RestControllerNotFoundException(String login, int id) {
        super(format(TASK_NOT_FOUND_FORMAT_MESSAGE, login, id));
    }

    public RestControllerNotFoundException(String login, int id, Throwable cause) {
        super(format(TASK_NOT_FOUND_FORMAT_MESSAGE, login, id), cause);
    }

    public RestControllerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestControllerNotFoundException(String message) {
        super(message);
    }
}
