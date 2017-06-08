package ru.glaizier.todo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
@Entity
public class Task {
    @Id
    private int id;
    private String login;
    private String todo;

    protected Task() {
    }

    public Task(int id, String login, String todo) {
        this.id = id;
        this.login = login;
        this.todo = todo;
    }
}
