package ru.glaizier.todo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
@Entity
public class Task {
    @Id
    // check for postgres generated table: https://stackoverflow.com/questions/39807483/sequence-hibernate-sequence-not-found-sql-statement
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String login;
    private String todo;

    protected Task() {
    }

    public Task(String login, String todo) {
        this(null, login, todo);
    }

    public Task(Integer id, String login, String todo) {
        this.id = id;
        this.login = login;
        this.todo = todo;
    }
}
