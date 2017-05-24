package ru.glaizier.todo.dao.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.UserDao;
import ru.glaizier.todo.domain.User;

import java.util.Arrays;
import java.util.Set;

@Repository
public class MemoryUserDao implements UserDao {

    private final MemoryDb db;

    @Autowired
    public MemoryUserDao(MemoryDb db) {
        this.db = db;
    }

    @Override
    public void addUser(User user) {
        db.addUser(user);
    }

    @Override
    public User getUser(String login) {
        return db.getUser(login);
    }

    @Override
    public User getUserWithPassword(String login, char[] password) {
        User user = getUser(login);
        return Arrays.equals(user.getPassword(), password) ? user : null;
    }

    @Override
    public User removeUser(String login) {
        return db.removeUser(login);
    }

    @Override
    public Set<User> getUsers() {
        return db.getUsers();
    }

    @Override
    public boolean containsUser(String login) {
        return getUser(login) != null;
    }
}
