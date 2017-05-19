package ru.glaizier.todo.dao;

import ru.glaizier.todo.domain.User;

import java.util.Set;

public interface UserDao {

    void addUser(User user);

    /**
     * @param login for User to get
     * @return User with such login or null if there is no User with such login
     */
    User getUser(String login);

    /**
     * @param login to remove
     * @return removed User or null if there is no User with such login
     */
    User removeUser(String login);

    Set<User> getUsers();

}
