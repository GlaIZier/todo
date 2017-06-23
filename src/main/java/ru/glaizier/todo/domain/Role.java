package ru.glaizier.todo.domain;

import static lombok.AccessLevel.NONE;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
public class Role {
    @Id
    private String role;

    @ManyToMany
    @Setter(NONE)
    private Set<User> users;
}
