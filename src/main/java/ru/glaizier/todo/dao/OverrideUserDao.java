package ru.glaizier.todo.dao;

import ru.glaizier.todo.domain.User;

public interface OverrideUserDao {

    User findUserByLogin(String login);

    User findUserByLoginAndPassword(String login, char[] password);

    <S extends User> S save(S s);

    void delete(String login);
}
