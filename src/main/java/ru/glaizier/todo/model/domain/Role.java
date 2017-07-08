package ru.glaizier.todo.model.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
@Table(name = "Role")
public class Role {
    public static Role USER = new Role("USER");
    public static Role ADMIN = new Role("ADMIN");

    @NonNull
    @Id
    @Column(unique = true, nullable = false)
    private String role;

    // Todo check cascade after all tests will be done
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
