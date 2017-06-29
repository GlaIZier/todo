package ru.glaizier.todo.model.domain;

import static lombok.AccessLevel.NONE;

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
import javax.persistence.ManyToMany;

@Getter
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
    @Setter(NONE)
    private Set<User> users;

    protected Role() {
    }
}
