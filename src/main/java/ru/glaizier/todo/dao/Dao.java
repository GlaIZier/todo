package ru.glaizier.todo.dao;

import ru.glaizier.todo.dao.exception.AccessDeniedException;
import ru.glaizier.todo.dao.task.TaskDao;
import ru.glaizier.todo.dao.user.UserDao;
import ru.glaizier.todo.domain.Task;

public interface Dao {

    TaskDao getTaskDao();

    UserDao getUserDao();

    Task findTaskUserJoined(Integer id);

    Task findTaskUserJoinedWithLoginCheck(Integer id, String login) throws AccessDeniedException;
}
