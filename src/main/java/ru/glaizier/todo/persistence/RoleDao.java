package ru.glaizier.todo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glaizier.todo.model.domain.Role;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, String>/*, OverrideRoleDao*/ {

    Role findRoleByRole(String role);

    List<Role> findAll();

    <S extends Role> S save(S s);

    void delete(String login);
}
