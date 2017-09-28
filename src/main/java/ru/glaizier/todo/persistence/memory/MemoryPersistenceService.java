package ru.glaizier.todo.persistence.memory;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.Persistence;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Service
@Profile("memory")
public class MemoryPersistenceService implements Persistence {

    private final ConcurrentMap<String, ConcurrentMap<Integer, TaskDto>> loginToIdToTask = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, UserDto> loginToUser = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, RoleDto> nameToRole = new ConcurrentHashMap<>();

    private AtomicInteger lastId = new AtomicInteger(0);

    private final InMemoryUserDetailsManager userDetailsManager;

    private final Object userDetailsLock = new Object();

    @Autowired
    public MemoryPersistenceService(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public List<TaskDto> findTasks() {
        return loginToIdToTask.entrySet().stream().collect(
                ArrayList::new,
                (acc, entry) -> acc.addAll(entry.getValue().values()),
                ArrayList::addAll
        );
    }

    @Override
    public List<TaskDto> findTasks(String login) {
        ConcurrentMap<Integer, TaskDto> idToTask = loginToIdToTask.get(login);
        return (idToTask == null) ? null : new ArrayList<>(idToTask.values());
    }

    @Override
    public TaskDto findTask(Integer id) {
        return loginToIdToTask.values().stream()
                .filter(idToTask -> idToTask.get(id) != null)
                .findFirst()
                .orElseGet(ConcurrentHashMap::new)
                .get(id);
    }

    @Override
    public TaskDto findTask(Integer id, String login) throws AccessDeniedException {
        UserDto user = findUser(login);
        if (user == null)
            return null;
        TaskDto task = findTask(id);
        if (task == null)
            return null;
        if (!task.getUser().orElseThrow(IllegalStateException::new).getLogin().equals(login))
            throw new AccessDeniedException(format("Can't get task with this %s id for %s", String.valueOf(id), login));
        return task;
    }

    @Override
    public TaskDto saveTask(String login, String todo) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(todo);

        UserDto userByLogin = loginToUser.get(login);
        if (userByLogin == null)
            return null;
        int newId = lastId.incrementAndGet();
        TaskDto savedTask = TaskDto.builder().id(newId).user(Optional.of(userByLogin)).todo(todo).build();
        loginToIdToTask.get(login).put(newId, savedTask);
        return savedTask;
    }

    @Override
    public TaskDto updateTask(String login, Integer id, String todo) throws AccessDeniedException {
        Objects.requireNonNull(login);
        Objects.requireNonNull(id);
        Objects.requireNonNull(todo);

        UserDto userByLogin = loginToUser.get(login);
        if (userByLogin == null)
            return null;

        TaskDto task = findTask(id);
        if (task == null)
            return null;

        if (!task.getUser().orElseThrow(IllegalStateException::new).getLogin().equals(login))
            throw new AccessDeniedException(format("User with login %s doesn't have rights to access task with %d id!",
                    login, id));

        TaskDto updatedTask = TaskDto.builder().id(id).user(task.getUser()).todo(todo).build();
        loginToIdToTask.get(login).put(id, updatedTask);
        return updatedTask;
    }

    @Override
    public TaskDto deleteTask(Integer id) {
        TaskDto task = findTask(id);
        if (task == null) {
            return null;
        }

        loginToIdToTask.values().stream()
                .filter(idToTask -> idToTask.get(id) != null)
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .remove(id);

        return task;
    }

    @Override
    public TaskDto deleteTask(Integer id, String login) throws AccessDeniedException {
        if (findTask(id, login) == null)
            return null;
        return deleteTask(id);
    }

    @Override
    public List<UserDto> findUsers() {
        return new ArrayList<>(loginToUser.values());
    }

    @Override
    public UserDto findUser(String login) {
        return loginToUser.get(login);
    }

    @Override
    public UserDto findUser(String login, char[] rawPassword) {
        UserDto user = findUser(login);
        if (user == null || !Arrays.equals(user.getPassword(), rawPassword))
            return null;
        return user;
    }

    @Override
    public UserDto saveUser(String login, char[] rawPassword, Set<RoleDto> roles) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(rawPassword);
        Objects.requireNonNull(roles);
//        if (roles.isEmpty())
//            throw new IllegalArgumentException("Roles are empty!");
        for (RoleDto role : roles)
            if (findRole(role.getRole()) == null)
                throw new JpaObjectRetrievalFailureException(new EntityNotFoundException("Role hasn't been found " + role.getRole()));

        UserDto newUser = UserDto.builder().login(login).password(rawPassword).roles(Optional.of(roles)).build();
        loginToUser.put(login, newUser);
        loginToIdToTask.put(login, new ConcurrentHashMap<>());

        synchronized (userDetailsLock) {
            if (!userDetailsManager.userExists(login))
                userDetailsManager.createUser(
                        User.withUsername(newUser.getLogin()).password(String.valueOf(newUser.getPassword()))
                                // Roles -> (to) Strings -> Remove ROLE_ from strings -> List of Strings -> array of Strings
                                .roles(newUser.getRoles().orElseThrow(IllegalStateException::new).stream()
                                        .map(RoleDto::getRole)
                                        .map(r -> r.replaceFirst("ROLE_", ""))
                                        .collect(Collectors.toList())
                                        .toArray(new String[newUser.getRoles().orElseThrow(IllegalStateException::new).size()]))
                                .build());
            else
                userDetailsManager.updateUser(User.withUsername(newUser.getLogin()).password(String.valueOf(newUser.getPassword()))
                        // Roles -> (to) Strings -> Remove ROLE_ from strings -> List of Strings -> array of Strings
                        .roles(newUser.getRoles().orElseThrow(IllegalStateException::new).stream()
                                .map(RoleDto::getRole)
                                .map(r -> r.replaceFirst("ROLE_", ""))
                                .collect(Collectors.toList())
                                .toArray(new String[newUser.getRoles().orElseThrow(IllegalStateException::new).size()]))
                        .build());
        }
        return newUser;
    }

    @Override
    public UserDto saveUser(String login, char[] rawPassword) {
        return saveUser(login, rawPassword, new HashSet<>(Collections.singletonList(RoleDto.USER)));
    }

    @Override
    public void deleteUser(String login) {
        loginToUser.remove(login);
        synchronized (userDetailsLock) {
            userDetailsManager.deleteUser(login);
        }
        loginToIdToTask.remove(login);
    }

    @Override
    public List<RoleDto> findRoles() {
        return new ArrayList<>(nameToRole.values());
    }

    @Override
    public RoleDto findRole(String role) {
        return nameToRole.get(role);
    }

    @Override
    public RoleDto saveRole(String role) {
        RoleDto newRole = new RoleDto(role);
        nameToRole.put(role, newRole);
        return newRole;
    }

    @Override
    public void deleteRole(String role) {
        RoleDto removedRole = nameToRole.remove(role);
        loginToUser.values().forEach(userDto -> {
            if (userDto.getRoles().isPresent()) {
                userDto.getRoles().get().remove(removedRole);
            }
        });
    }
}
