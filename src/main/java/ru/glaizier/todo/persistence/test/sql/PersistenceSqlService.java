package ru.glaizier.todo.persistence.test.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.model.dto.Authorization;
import ru.glaizier.todo.persistence.exception.PersistenceException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@Profile({"default", "prod"})
public class PersistenceSqlService implements PersistenceSql {

    private DataSource dataSource;

    @Autowired
    public PersistenceSqlService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Authorization> getAuthorizations() {
        List<Authorization> result = new ArrayList<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM AUTHORIZATION");
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                result.add(new Authorization(rs.getString("login"), rs.getString("role")));
            }
            return result;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    public void test() throws SQLException {
        try (Connection c = dataSource.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM Task");
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getString(3));
            }
        }
    }
}
