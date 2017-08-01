package ru.glaizier.todo.persistence;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.init.annotation.PostContextUp;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;
import ru.glaizier.todo.persistence.exception.AccessDeniedException;
import ru.glaizier.todo.persistence.role.RoleDao;
import ru.glaizier.todo.persistence.task.TaskDao;
import ru.glaizier.todo.persistence.user.UserDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

@Service
@Transactional
public class PersistenceService implements Persistence {

    private final TaskDao taskDao;

    private final UserDao userDao;

    private final RoleDao roleDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersistenceService(
            TaskDao taskDao,
            UserDao userDao,
            RoleDao roleDao,
            PasswordEncoder passwordEncoder) {
        this.taskDao = taskDao;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @PostContextUp
    @Override
    public void initDb() {
        RoleDto userRole = new RoleDto(Role.USER.getRole());
        RoleDto adminRole = new RoleDto(Role.ADMIN.getRole());
        saveRole(userRole.getRole());
        saveRole(adminRole.getRole());

        HashSet<RoleDto> uRoles = new HashSet<>(Arrays.asList(userRole));
        HashSet<RoleDto> aRoles = new HashSet<>(Arrays.asList(userRole, adminRole));
        UserDto u = saveUser("u", "p".toCharArray(), uRoles);
        UserDto a = saveUser("a", "p".toCharArray(), aRoles);

        saveTask(u.getLogin(), "todo1");
        saveTask(u.getLogin(), "todo2");
        saveTask(a.getLogin(), "todo1");
    }

    @Override
    public List<TaskDto> findTasks(String login) {
        User user = userDao.findUserByLogin(login);
        if (user == null)
            return null;
        List<Task> tasksByUser = taskDao.findTasksByUser(user);
        if (tasksByUser == null)
            return new ArrayList<>();
        return tasksByUser.stream().collect(ArrayList::new,
                (acc, t) -> {
                    acc.add(new TaskDto(t.getId(), Optional.empty(), t.getTodo()));
                }, ArrayList::addAll);
    }

    @Override
    public TaskDto findTask(Integer id) {
        Task taskById = taskDao.findTaskById(id);
        if (taskById == null)
            return null;
        User user = taskById.getUser();
        Set<Role> roles = user.getRoles();
        UserDto userDto = UserDto.builder().login(user.getLogin()).password(user.getPassword()).roles(Optional.of(transformRoles(roles))).build();
        return new TaskDto(taskById.getId(), Optional.of(userDto), taskById.getTodo());
    }

    @Override
    public TaskDto findTask(Integer id, String login) throws AccessDeniedException {
        Task task = taskDao.findTaskById(id);
        if (task == null)
            return null;

        if (userDao.findUserByLogin(login) == null)
            return null;

        String taskLogin = task.getUser().getLogin();
        if (!taskLogin.equals(login))
            throw new AccessDeniedException(format("User with login %s doesn't have rights to access task with %d id!",
                    login, id));

        return new TaskDto(id, Optional.empty(), task.getTodo());
    }

    @Override
    public TaskDto saveTask(String login, String todo) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(todo);

        User userByLogin = userDao.findUserByLogin(login);
        if (userByLogin == null)
            return null;
        Task task = taskDao.save(Task.builder().user(userByLogin).todo(todo).build());
        return TaskDto.builder().id(task.getId()).user(Optional.empty()).todo(task.getTodo()).build();
    }

    @Override
    public TaskDto updateTask(String login, Integer id, String todo) throws AccessDeniedException {
        Objects.requireNonNull(login);
        Objects.requireNonNull(id);
        Objects.requireNonNull(todo);

        User user = userDao.findUserByLogin(login);
        if (user == null)
            return null;

        Task task = taskDao.findTaskById(id);
        if (!task.getUser().getLogin().equals(login))
            throw new AccessDeniedException(format("User with login %s doesn't have rights to access task with %d id!",
                    login, id));
        taskDao.save(task.toBuilder().todo(todo).build());

        return TaskDto.builder().id(id).user(Optional.empty()).todo(todo).build();
    }

    @Override
    public TaskDto deleteTask(Integer id) {
        Task task = taskDao.findTaskById(id);
        if (task == null) {
            return null;
        }
        User user = task.getUser();
        Set<Role> roles = user.getRoles();
        TaskDto taskDto = TaskDto.builder().id(task.getId())
                .user(Optional.of(UserDto.builder().login(user.getLogin()).password(user.getPassword())
                        .roles(Optional.of(transformRoles(roles))).build()))
                .todo(task.getTodo())
                .build();
        taskDao.delete(id);
        return taskDto;
    }

    @Override
    public TaskDto deleteTask(Integer id, String login) {
        if (findTask(id, login) == null)
            return null;
        return deleteTask(id);
    }

    @Override
    public UserDto findUser(String login) {
        User user = userDao.findUserByLogin(login);
        if (user == null)
            return null;
        Set<Role> roles = user.getRoles();
        Objects.requireNonNull(roles);
        return UserDto.builder().login(user.getLogin()).password(user.getPassword()).roles(Optional.of(transformRoles(roles))).build();
    }

    @Override
    public List<UserDto> findUsers() {
        List<User> users = userDao.findAll();
        return users.stream().collect(ArrayList::new, (acc, user) -> {
            acc.add(UserDto.builder().login(user.getLogin()).password(user.getPassword()).roles(Optional.of(transformRoles(user.getRoles()))).build());
        }, ArrayList::addAll);
    }

    @Override
    public UserDto findUser(String login, char[] rawPassword) {
        UserDto user = findUser(login);
        if (user == null || !passwordEncoder.matches(String.valueOf(rawPassword), String.valueOf(user.getPassword())))
            return null;
        return user;
    }

    @Override
    public UserDto saveUser(String login, char[] rawPassword, Set<RoleDto> roles) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(rawPassword);
        Objects.requireNonNull(roles);

        char[] encodedPassword = passwordEncoder.encode(String.valueOf(rawPassword)).toCharArray();
        User createdUser = User.builder().login(login).password(encodedPassword)
                .roles(transformRoleDtos(roles))
                .build();
        User user = userDao.save(createdUser);

        return UserDto.builder().login(user.getLogin()).password(user.getPassword())
                .roles(Optional.of(transformRoles(user.getRoles()))).build();
    }

    @Override
    public UserDto saveUser(String login, char[] rawPassword) {
        return saveUser(login, rawPassword, new HashSet<>(Collections.singletonList(RoleDto.USER)));
    }

    @Override
    public void deleteUser(String login) {
        userDao.delete(login);
    }

    @Override
    public List<RoleDto> findRoles() {
        return transformRoles(roleDao.findAll());
    }

    @Override
    public RoleDto findRole(String role) {
        Role roleByRole = roleDao.findRoleByRole(role);
        return (roleByRole == null) ? null : new RoleDto(roleByRole.getRole());
    }

    @Override
    public RoleDto saveRole(String role) {
        Role savedRole = roleDao.save(new Role(role));
        return savedRole == null ? null : new RoleDto(savedRole.getRole());
    }

    @Override
    public void deleteRole(String role) {
        roleDao.delete(role);
    }

    private Set<Role> transformRoleDtos(Set<RoleDto> roleDtos) {
        return roleDtos.stream().collect(HashSet::new, (a, r) -> a.add(new Role(r.getRole())), HashSet::addAll);
    }

    private List<Role> transformRoleDtos(List<RoleDto> roleDtos) {
        return roleDtos.stream().collect(ArrayList::new, (a, r) -> a.add(new Role(r.getRole())), ArrayList::addAll);
    }

    private Set<RoleDto> transformRoles(Set<Role> roles) {
        return roles.stream().collect(HashSet::new, (a, r) -> a.add(new RoleDto(r.getRole())), HashSet::addAll);
    }

    private List<RoleDto> transformRoles(List<Role> roles) {
        return roles.stream().collect(ArrayList::new, (a, r) -> a.add(new RoleDto(r.getRole())), ArrayList::addAll);
    }

}
