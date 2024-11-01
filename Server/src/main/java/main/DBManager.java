package main;
import java.sql.*;
import java.util.Properties;

public class DBManager {
    public Connection connect(String dbName, String userName, String password) {
        String url = "jdbc:postgresql://localhost/" + dbName;
        Properties props = new Properties();
        props.setProperty("user", userName);
        props.setProperty("password", password);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            throw new RuntimeException(e);
        }
        return conn;
    }
}
