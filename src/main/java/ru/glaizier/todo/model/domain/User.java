package ru.glaizier.todo.model.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
// Exclude tasks to avoid cyclic dependencies between user and task
@EqualsAndHashCode(exclude = "tasks")
@ToString(exclude = {"tasks", "password"})
@Entity
@Table(name = "User")
public class User {
    @NonNull
    @Id
    @Column(unique = true, nullable = false)
    private String login;

    @NonNull
    @Column(nullable = false, length = 30)
    private char[] password;

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = PERSIST)
    private Set<Task> tasks;

    @ManyToMany(fetch = LAZY
//            , cascade = {
//            CascadeType.ALL
//    }
    )
    @JoinTable(name = "Authorization",
            joinColumns = @JoinColumn(name = "login", referencedColumnName = "login", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "role", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<Role> roles;

    protected User() {
    }

    @Builder(toBuilder = true)
    public User(String login, char[] password, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}
