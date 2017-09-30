package ru.glaizier.todo.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.model.domain.User;

import java.util.List;

public interface UserDao extends JpaRepository<User, String>/*, OverrideUserDao*/ {

    User findUserByLogin(String login);

    User findUserByLoginAndPassword(String login, char[] password);

    List<User> findAll();

    <S extends User> S save(S s);

    void delete(String login);
}
