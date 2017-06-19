package ru.glaizier.todo.dao.memory;

import ru.glaizier.todo.domain.User;

import java.util.Set;

@Deprecated
// Todo remove it. Check webSecurityConfig before
// Todo update readme to maintain memory and embedded db
public interface UserDao {

    void addUser(User user);

    /**
     * @param login for User to get
     * @return User with such login or null if there is no User with such login
     */
    User getUser(String login);

    /**
     * @param login    for User to get
     * @param password for User to get
     * @return User with such login and password or null if there is no User with such login or password in incorrect.
     * Use getUser(String login) to ensure that User is present
     */
    User getUserWithPassword(String login, char[] password);

    /**
     * @param login to remove
     * @return removed User or null if there is no User with such login
     */
    User removeUser(String login);

    Set<User> getUsers();

    boolean containsUser(String login);

}
