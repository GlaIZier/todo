package ru.glaizier.todo.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
public class Role {
    public static Role USER = new Role("USER");
    public static Role ADMIN = new Role("ADMIN");

    @NonNull
    @Id
    private String role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Authorization",
            joinColumns = @JoinColumn(name = "role", referencedColumnName = "role"),
            inverseJoinColumns = @JoinColumn(name = "login", referencedColumnName = "login"))
    private Set<User> users;

    protected Role() {
    }
}
