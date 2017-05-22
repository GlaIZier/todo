package ru.glaizier.todo.controller.api.exception;

import static java.lang.String.format;

public class ApiTaskNotFoundException extends ApiNotFoundException {

    public final static String TASK_NOT_FOUND_FORMAT_MESSAGE = "Task for user %s with id %d hasn't been found!";

    public ApiTaskNotFoundException(String login, int id) {
        super(format(TASK_NOT_FOUND_FORMAT_MESSAGE, login, id));
    }

    public ApiTaskNotFoundException(String login, int id, Throwable cause) {
        super(format(TASK_NOT_FOUND_FORMAT_MESSAGE, login, id), cause);
    }

}
