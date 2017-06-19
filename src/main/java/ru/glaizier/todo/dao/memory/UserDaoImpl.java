package ru.glaizier.todo.dao.memory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import ru.glaizier.todo.dao.OverrideUserDao;
import ru.glaizier.todo.domain.User;

import java.util.Arrays;

@Repository
@Profile("memory")
public class UserDaoImpl implements OverrideUserDao {

    private final MemoryDb db;

    @Autowired
    public UserDaoImpl(MemoryDb db) {
        this.db = db;
    }

    @Override
    public User findUserByLogin(String login) {
        return db.getUser(login);
    }

    @Override
    public User findUserByLoginAndPassword(String login, char[] password) {
        User user = findUserByLogin(login);
        return user == null ?
                null
                :
                Arrays.equals(user.getPassword(), password) ? user : null;
    }

    @Override
    public <S extends User> S save(S s) {
        db.addUser(s);
        return s;
    }

    @Override
    public void delete(String login) {
        db.removeUser(login);
    }
}
