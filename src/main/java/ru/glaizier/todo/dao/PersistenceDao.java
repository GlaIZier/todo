package ru.glaizier.todo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class PersistenceDao implements Dao {

    private TaskDao taskDao;

    private UserDao userDao;

    @Autowired
    private PersistenceDao(TaskDao taskDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }


}
