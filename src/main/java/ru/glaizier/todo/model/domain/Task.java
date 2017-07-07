package ru.glaizier.todo.model.domain;

import static javax.persistence.FetchType.LAZY;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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
    @JoinColumn(name = "login",
            referencedColumnName = "login",
            nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
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
