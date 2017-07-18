package ru.glaizier.todo.model.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
@Table(name = "Role")
public class Role {
    public static Role USER = new Role("ROLE_USER");
    public static Role ADMIN = new Role("ROLE_ADMIN");

    @NonNull
    @Id
    @Column(unique = true, nullable = false)
    private String role;

    @ManyToMany(fetch = FetchType.LAZY
//            , cascade = {
//            CascadeType.REMOVE
//    }
    )
    @JoinTable(name = "Authorization",
            joinColumns = @JoinColumn(name = "role", referencedColumnName = "role", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "login", referencedColumnName = "login", nullable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<User> users;

    protected Role() {
    }
}
