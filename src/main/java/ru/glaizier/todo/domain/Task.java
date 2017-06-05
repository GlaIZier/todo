package ru.glaizier.todo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Entity
public class Task {
    @Id
    private int id;
    private String todo;

    protected Task() {
    }

    public Task(int id, String todo) {
        this.id = id;
        this.todo = todo;
    }
}
