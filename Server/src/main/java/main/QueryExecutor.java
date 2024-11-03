package main;

import org.example.Area;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class QueryExecutor {
    private Connection conn = null;
    public QueryExecutor(String dbName, String username, String password){
        this.conn = connect(dbName, username, password);
    }

    public Connection connect(String dbName, String userName, String password) {
        String url = "jdbc:postgresql://localhost/" + dbName;
        Properties props = new Properties();
        props.setProperty("user", userName);
        props.setProperty("password", password);
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            throw new RuntimeException(e);
        }
        return conn;
    }

    //Select
    public ArrayList<Area> select_all_geographic_areas(){
        ArrayList<Area> areas = new ArrayList<>();
        try (Statement st = conn.prepareStatement("SELECT denominazione, stato, latitudine, longitudine FROM aree_geografiche;");
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String denominazione = rs.getString(1);
                String stato = rs.getString(2);
                double latitudine = Double.parseDouble(rs.getString(3));
                double longitudine = Double.parseDouble(rs.getString(4));
                Area a = new Area(denominazione, stato, latitudine, longitudine);
                areas.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get areas from the database");
        }
        return areas;
    }
}
