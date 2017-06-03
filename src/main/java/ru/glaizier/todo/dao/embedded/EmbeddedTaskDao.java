package ru.glaizier.todo.dao.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class EmbeddedTaskDao {

    private DataSource dataSource;

    @Autowired
    public EmbeddedTaskDao(DataSource dataSource) {
        this.dataSource = dataSource;
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
