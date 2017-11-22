package ru.glaizier.todo.persistence;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.properties.PropertiesService;

/**
 * Class to init db with test data for hsql implementation. Tests use this information.
 */
@Component
public class PersistenceInit {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Persistence persistence;

    private final Environment environment;

    private final PropertiesService propertiesService;

    @Autowired
    public PersistenceInit(Persistence persistence,
                           Environment environment,
                           PropertiesService propertiesService) {
        this.persistence = persistence;
        this.environment = environment;
        this.propertiesService = propertiesService;
    }

    @PostConstruct
    public void createMockData() {
        List<RoleDto> roles = createRoles();

        String[] activeProfiles = environment.getActiveProfiles();
        String profile;
        if (activeProfiles.length == 0) {
            profile = propertiesService.getSpringProfilesDefaultName();
            log.info(
                "Couldn't find an active profile in the environment. The default profile from properties file has been used");
        } else {
            profile = activeProfiles[0];
            log.info("Active profile {} has been found in the environment", profile);
        }

        if (!profile.equalsIgnoreCase(propertiesService.getSpringProfilesProdName())) {
            createUsersAndTasks(roles);
            log.info("Mock data ('a', 'u' users and tasks for them) for '{}' profile has been created", profile);
        }
    }

    // Create roles only if they are absent because we need to save existing roles and authorizations
    // if they are present in the db
    private List<RoleDto> createRoles() {
        List<RoleDto> roles = new ArrayList<>();
        RoleDto userRole = new RoleDto(Role.USER.getRole());
        if (persistence.findRole(userRole.getRole()) == null) {
            persistence.saveRole(userRole.getRole());
            log.info("Basic role 'ROLE_USER' has been created");
        } else {
            log.info("Basic role 'ROLE_USER' has been found. Skip creating 'ROLE_USER'");
        }
        roles.add(userRole);

        RoleDto adminRole = new RoleDto(Role.ADMIN.getRole());
        if (persistence.findRole(adminRole.getRole()) == null) {
            persistence.saveRole(adminRole.getRole());
            log.info("Basic role 'ROLE_ADMIN' has been created");
        } else {
            log.info("Basic role 'ROLE_ADMIN' has been found. Skip creating 'ROLE_ADMIN'");
        }
        roles.add(adminRole);

        return roles;
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
