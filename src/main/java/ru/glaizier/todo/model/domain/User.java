package ru.glaizier.todo.model.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Getter
@Setter
// Exclude tasks to avoid cyclic dependencies between user and task
@EqualsAndHashCode(exclude = "tasks")
@Entity
// Todo remove setters?
public class User {
    @NonNull
    @Id
    private String login;

    @NonNull
    private char[] password;

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = ALL)
    @Setter(AccessLevel.NONE)
    private Set<Task> tasks;

    @ManyToMany(cascade = ALL, fetch = LAZY)
    @JoinTable(name = "Authorization",
            joinColumns = @JoinColumn(name = "login", referencedColumnName = "login"),
            inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "role"))
    private Set<Role> roles;

    protected User() {
    }

    @Builder
    public User(String login, char[] password, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public String toString() {
        return "ru.glaizier.todo.model.domain.User(login=" + this.getLogin() + ", roles=" + this.getRoles() + ")";
    }
}
