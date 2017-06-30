package ru.glaizier.todo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.dao.task.TaskDao;
import ru.glaizier.todo.dao.user.UserDao;
import ru.glaizier.todo.model.domain.Role;
import ru.glaizier.todo.model.domain.Task;
import ru.glaizier.todo.model.domain.User;
import ru.glaizier.todo.model.dto.TaskDto;
import ru.glaizier.todo.model.dto.UserDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
    public List<TaskDto> findTasksByUser(User user) {

        return null;
    }

    @Override
    public TaskDto findTaskById(Integer id) {
        return null;
    }

    @Override
    public <S extends TaskDto> S saveTask(S s) {
        return null;
    }

    @Override
    public void deleteTask(Integer integer) {

    }

    @Override
    public UserDto findUserByLogin(String login) {
        return null;
    }

    @Override
    public UserDto findUserByLoginAndPassword(String login, char[] password) {
        return null;
    }

    @Override
    public <S extends UserDto> S save(S s) {
        return null;
    }

    @Override
    public void deleteUser(String login) {

    }

    @Override
    public TaskDto findTaskByIdAndLogin(Integer id, String login) throws AccessDeniedException {
        Task task = taskDao.findTaskById(id);
        if (task == null)
            return null;
        String login1 = task.getUser().getLogin();
        Set<String> taskUserRoles = task.getUser().getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
        return new TaskDto(id, null, task.getTodo());
    }

}
