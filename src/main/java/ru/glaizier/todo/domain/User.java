package ru.glaizier.todo.domain;

import static javax.persistence.FetchType.LAZY;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
@Setter
// Exclude tasks to avoid cyclic dependencies between user and task
@EqualsAndHashCode(exclude = "tasks")
@ToString(exclude = {"tasks", "password"})
@Entity
public class User {
    @Id
    private String login;
    private char[] password;
    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Task> tasks;

    protected User() {
    }

    public User(String login, char[] password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, char[] password, Set<Task> tasks) {
        this.login = login;
        this.password = password;
        this.tasks = tasks;
    }
}
