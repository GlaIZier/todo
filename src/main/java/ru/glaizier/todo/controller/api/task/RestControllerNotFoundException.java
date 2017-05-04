package ru.glaizier.todo.controller.api.task;

import static java.lang.String.format;

public class RestControllerNotFoundException extends RestControllerException {

    private final static String msgToFormat = "Task for user %s with id %d hasn't been found!";

    private final String login;

    private final int id;

    public RestControllerNotFoundException(String login, int id) {
        super(format(msgToFormat, login, id));
        this.login = login;
        this.id = id;
    }

    public RestControllerNotFoundException(String login, int id, Throwable cause) {
        super(format(msgToFormat, login, id), cause);
        this.login = login;
        this.id = id;
    }
}
