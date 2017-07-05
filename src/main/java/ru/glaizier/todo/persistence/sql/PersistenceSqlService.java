package ru.glaizier.todo.persistence.sql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.glaizier.todo.persistence.exception.PersistenceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;


@Service
public class PersistenceSqlService implements PersistenceSql {

    private DataSource dataSource;

    @Autowired
    public PersistenceSqlService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Map<String, String> getAllFromAuthorization() {
        Map<String, String> result = new TreeMap<>();
        try (Connection c = dataSource.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM AUTHORIZATION");
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("login"), rs.getString("role"));
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
