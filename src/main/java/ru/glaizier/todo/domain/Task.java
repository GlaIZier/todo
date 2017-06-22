package ru.glaizier.todo.domain;

import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
@Entity
public class Task {
    @Id
    // check for postgres generated table: https://stackoverflow.com/questions/39807483/sequence-hibernate-sequence-not-found-sql-statement
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "login")
    private User user;
    private String todo;

    protected Task() {
    }

    public Task(User user, String todo) {
        this(null, user, todo);
    }

    public Task(Integer id, User user, String todo) {
        this.id = id;
        this.user = user;
        this.todo = todo;
    }
}
