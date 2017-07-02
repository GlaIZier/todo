package ru.glaizier.todo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.dao.task.TaskDao;
import ru.glaizier.todo.dao.user.UserDao;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.model.dto.RoleDto;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;

import javax.transaction.Transactional;
import java.util.*;

import static java.lang.String.format;

@Service
@Transactional
public class PersistenceService implements Persistence {

    private final TaskDao taskDao;

    private final UserDao userDao;

    @Autowired
    private PersistenceService(TaskDao taskDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }

    @Override
    public List<TaskDto> findTasksByLogin(String login) {
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
    public TaskDto findTaskById(Integer id) {
        Task taskById = taskDao.findTaskById(id);
        if (taskById == null)
            return null;
        return new TaskDto(taskById.getId(), Optional.empty(), taskById.getTodo());
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
    public TaskDto saveTask(String login, Integer id, String todo) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(id);
        Objects.requireNonNull(todo);

        User userByLogin = userDao.findUserByLogin(login);
        if (userByLogin == null)
            return null;

        Task task = taskDao.save(Task.builder().id(id).user(userByLogin).todo(todo).build());
        return TaskDto.builder().id(task.getId()).user(Optional.empty()).todo(task.getTodo()).build();
    }

    @Override
    public void deleteTask(Integer id) {
        taskDao.delete(id);
    }

    @Override
    public UserDto findUserByLogin(String login) {
        User user = userDao.findUserByLogin(login);
        if (user == null)
            return null;
        Set<Role> roles = user.getRoles();
        Objects.requireNonNull(roles);
        Set<RoleDto> roleDtos = roles.stream().collect(HashSet::new, (a, r) -> a.add(new RoleDto(r.getRole())), HashSet::addAll);
        return UserDto.builder().login(user.getLogin()).password(user.getPassword()).roles(Optional.of(roleDtos)).build();
    }

    @Override
    public UserDto findUserByLoginAndPassword(String login, char[] password) {
        UserDto user = findUserByLogin(login);
        if (user == null || !Arrays.equals(password, user.getPassword()))
            return null;
        return user;
    }

    @Override
    public UserDto saveUser(String login, char[] password, Set<RoleDto> roles) {
        Objects.requireNonNull(login);
        Objects.requireNonNull(password);
        Objects.requireNonNull(roles);

        User createdUser = User.builder().login(login).password(password)
                .roles(transformRoleDtos(roles))
                .build();
        User user = userDao.save(createdUser);

        return UserDto.builder().login(user.getLogin()).password(user.getPassword())
                .roles(Optional.of(transformRoles(user.getRoles()))).build();
    }

    @Override
    public void deleteUser(String login) {
        userDao.delete(login);
    }

    @Override
    public TaskDto findTaskByIdAndLogin(Integer id, String login) throws AccessDeniedException {
        Task task = taskDao.findTaskById(id);
        if (task == null)
            return null;

        String taskLogin = task.getUser().getLogin();
        if (!taskLogin.equals(login))
            throw new AccessDeniedException(format("User with login %s doesn't have rights to access task with %d id!",
                    login, id));

        return new TaskDto(id, Optional.empty(), task.getTodo());
    }

    private Set<Role> transformRoleDtos(Set<RoleDto> roleDtos) {
        return roleDtos.stream().collect(HashSet::new, (a, r) -> a.add(new Role(r.getRole())), HashSet::addAll);
    }

    private Set<RoleDto> transformRoles(Set<Role> roles) {
        return roles.stream().collect(HashSet::new, (a, r) -> a.add(new RoleDto(r.getRole())), HashSet::addAll);
    }

}
