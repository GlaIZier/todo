package ru.glaizier.todo.persistence.test.sql;

import ru.glaizier.todo.model.dto.Authorization;

import java.util.List;

public interface PersistenceSql {

    List<Authorization> getAuthorizations();

}
