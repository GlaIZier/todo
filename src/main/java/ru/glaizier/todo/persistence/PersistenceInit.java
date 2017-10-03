package ru.glaizier.todo.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.UserDto;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * Class to init db with test data for hsql implementation. Tests use this information.
 */
@Component
//@Profile({"default", "memory"})
// Todo change memory tests to support this init data
public class PersistenceInit {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String PROD_PROFILE = "prod";

    private Persistence persistence;

    private Environment environment;

    @Autowired
    public PersistenceInit(Persistence persistence,
                           Environment environment) {
        this.persistence = persistence;
        this.environment = environment;
    }

    @PostConstruct
    public void createMockData() {
        List<RoleDto> roles = createRoles();

        String profile = environment.getActiveProfiles()[0];
        log.info("Found {} profile", profile);
        if (!profile.equalsIgnoreCase(PROD_PROFILE)) {
            createUsersAndTasks(roles);
        }

    }

    private List<RoleDto> createRoles() {
        RoleDto userRole = new RoleDto(Role.USER.getRole());
        RoleDto adminRole = new RoleDto(Role.ADMIN.getRole());
        persistence.saveRole(userRole.getRole());
        persistence.saveRole(adminRole.getRole());

        return Arrays.asList(userRole, adminRole);
    }

    private void createUsersAndTasks(List<RoleDto> roles) {
        HashSet<RoleDto> uRoles = new HashSet<>(Arrays.asList(roles.get(0)));
        HashSet<RoleDto> aRoles = new HashSet<>(Arrays.asList(roles.get(0), roles.get(1)));
        UserDto u = persistence.saveUser("u", "p".toCharArray(), uRoles);
        UserDto a = persistence.saveUser("a", "p".toCharArray(), aRoles);

        persistence.saveTask(u.getLogin(), "todo1");
        persistence.saveTask(u.getLogin(), "todo2");
        persistence.saveTask(a.getLogin(), "todo1");
    }
}
