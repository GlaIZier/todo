package ru.glaizier.todo.model.domain;

import static javax.persistence.FetchType.LAZY;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode()
@ToString()
@Entity
@Table(name = "Task")
public class Task {
    @Id
    // check for postgres generated table: https://stackoverflow.com/questions/39807483/sequence-hibernate-sequence-not-found-sql-statement
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @NonNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "login")
    private User user;

    @NonNull
    @Column(nullable = false)
    private String todo;

    protected Task() {
    }

    public Task(User user, String todo) {
        this(null, user, todo);
    }

    @Builder(toBuilder = true)
    public Task(Integer id, User user, String todo) {
        this.id = id;
        this.user = user;
        this.todo = todo;
    }
}
