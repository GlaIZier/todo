package ru.glaizier.todo.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

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
