package ru.glaizier.todo.dao.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;


@Repository
public class EmbeddedTaskDaoSql {

    private DataSource dataSource;

    @Autowired
    public EmbeddedTaskDaoSql(DataSource dataSource) {
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
