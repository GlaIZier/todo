package ru.glaizier.todo.dao;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.dao.task.TaskDao;
import ru.glaizier.todo.dao.user.UserDao;
import ru.glaizier.todo.domain.dto.TaskDto;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;

import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Repository
@Transactional
public class DaoService implements Dao {

    private TaskDao taskDao;

    private UserDao userDao;

    @Autowired
    private DaoService(TaskDao taskDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }


    @Override
    public TaskDao getTaskDao() {
        return taskDao;
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    @Transactional
    public Task findTaskUserJoined(Integer id) {
        Task task = taskDao.findTaskById(id);
        if (task == null)
            return null;
        User taskUser = task.getUser();
        Set<Role> taskUserRoles = taskUser.getRoles();
        User user = User.builder().login(taskUser.getLogin()).password(taskUser.getPassword()).roles(taskUserRoles).build();
        return Task.builder().id(task.getId()).user(user).todo(task.getTodo()).build();
    }

    @Override
    @Transactional
    public Task findTaskUserJoinedWithLoginCheck(Integer id, String login) {
        Task task = findTaskUserJoined(id);
        if (task == null)
            return null;
        if (!task.getUser().getLogin().equals(login))
            throw new AccessDeniedException(format("%s is forbidden to get task with %s id", login, id));
        return task;
    }

    @Transactional
    public TaskDto testDto(Integer id) {
        Task task = taskDao.findTaskById(id);
        if (task == null)
            return null;
        String login = task.getUser().getLogin();
        Set<String> taskUserRoles = task.getUser().getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
        return new TaskDto(id, login, taskUserRoles, task.getTodo());
    }
}
