package ru.glaizier.todo.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.UserDto;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class to init db with test data for hsql implementation. Tests use this information.
 */
@Component
public class PersistenceInit {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    // Todo move it to properties file and change logic of create mock data
    private static final String prodProfile = "prod";
    private static final String defaultProfile = "default";

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

        String[] activeProfiles = environment.getActiveProfiles();
        String profile;
        if (activeProfiles.length == 0)
            profile = defaultProfile;
        else
            profile = activeProfiles[0];

        log.info("Creating mock data for {} profile", profile);
        if (!profile.equalsIgnoreCase(prodProfile))
            createUsersAndTasks(roles);
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
