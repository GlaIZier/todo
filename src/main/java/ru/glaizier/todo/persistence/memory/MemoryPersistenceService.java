package ru.glaizier.todo.persistence.memory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.Persistence;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Profile("memory")
public class MemoryPersistenceService implements Persistence {

    private final ConcurrentMap<String, ConcurrentMap<Integer, TaskDto>> loginToIdToTask = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, UserDto> loginToUser = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, RoleDto> nameToRole = new ConcurrentHashMap<>();

    private AtomicInteger lastId = new AtomicInteger(0);

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
        return null;
    }

    @Override
    public TaskDto findTask(Integer id) {
        return null;
    }

    @Override
    public TaskDto findTask(Integer id, String login) throws AccessDeniedException {
        return null;
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
        return null;
    }

    @Override
    public TaskDto deleteTask(Integer id) {
        return null;
    }

    @Override
    public TaskDto deleteTask(Integer id, String login) throws AccessDeniedException {
        return null;
    }

    @Override
    public List<UserDto> findUsers() {
        return new ArrayList<>(loginToUser.values());
    }

    @Override
    public UserDto findUser(String login) {
        return null;
    }

    @Override
    public UserDto findUser(String login, char[] rawPassword) {
        return null;
    }

    @Override
    public UserDto saveUser(String login, char[] rawPassword, Set<RoleDto> roles) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(rawPassword);
        Objects.requireNonNull(roles);
        for (RoleDto role : roles)
            if (findRole(role.getRole()) == null)
                throw new IllegalArgumentException("Role hasn't been found " + role.getRole());

        UserDto newUser = UserDto.builder().login(login).password(rawPassword).roles(Optional.of(roles)).build();
        loginToUser.put(login, newUser);
        loginToIdToTask.put(login, new ConcurrentHashMap<>());

        return newUser;
    }

    @Override
    public UserDto saveUser(String login, char[] rawPassword) {
        return saveUser(login, rawPassword, new HashSet<>(Collections.singletonList(RoleDto.USER)));
    }

    @Override
    public void deleteUser(String login) {

    }

    @Override
    public List<RoleDto> findRoles() {
        return null;
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

    }
}
