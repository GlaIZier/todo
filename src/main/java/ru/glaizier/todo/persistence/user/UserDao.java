package ru.glaizier.todo.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.model.domain.User;

public interface UserDao extends JpaRepository<User, String>/*, OverrideUserDao*/ {

    User findUserByLogin(String login);

    User findUserByLoginAndPassword(String login, char[] password);

    <S extends User> S save(S s);

    void delete(String login);
}
