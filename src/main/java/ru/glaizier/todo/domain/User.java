package ru.glaizier.todo.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@Entity
public class User {
    @Id
    private String login;
    private char[] password;

    protected User() {
    }

    public User(String login, char[] password) {
        this.login = login;
        this.password = password;
    }
}
