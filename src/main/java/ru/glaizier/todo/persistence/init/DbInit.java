package ru.glaizier.todo.persistence.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.Persistence;

import java.util.Arrays;
import java.util.HashSet;

import javax.annotation.PostConstruct;

@Configuration
@Profile("default")
public class DbInit {

    private Persistence persistence;

    @Autowired
    public DbInit(Persistence persistence) {
        this.persistence = persistence;
    }

    @PostConstruct
    public void createMockData() {
        RoleDto userRole = new RoleDto(Role.USER.getRole());
        RoleDto adminRole = new RoleDto(Role.ADMIN.getRole());
        persistence.saveRole(userRole.getRole());
        persistence.saveRole(adminRole.getRole());

        HashSet<RoleDto> uRoles = new HashSet<>(Arrays.asList(userRole));
        HashSet<RoleDto> aRoles = new HashSet<>(Arrays.asList(userRole, adminRole));
        UserDto u = persistence.saveUser("u", "p".toCharArray(), uRoles);
        UserDto a = persistence.saveUser("a", "p".toCharArray(), aRoles);

        persistence.saveTask(u.getLogin(), "todo1");
        persistence.saveTask(u.getLogin(), "todo2");
        persistence.saveTask(a.getLogin(), "todo1");
    }
}
