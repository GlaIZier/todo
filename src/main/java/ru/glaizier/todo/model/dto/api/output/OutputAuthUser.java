package ru.glaizier.todo.model.dto.api.output;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OutputAuthUser extends OutputUser {
    private final String token;

    public OutputAuthUser(String login, String token) {
        super(login);
        this.token = token;
    }
}
