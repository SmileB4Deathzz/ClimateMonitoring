package main;

import org.example.Area;
import org.example.MonitoringCenter;
import org.example.Operator;
import org.example.Parameter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class QueryExecutor {
    private Connection conn = null;

    public QueryExecutor(String dbName, String username, String password) {
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
    public ArrayList<Area> select_all_geographic_areas() {
        ArrayList<Area> areas = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT denominazione, stato, latitudine, longitudine FROM aree_geografiche;")) {
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

    public ArrayList<Parameter> select_area_parameters(Area area) {
        ArrayList<Parameter> params = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement("""
                SELECT parametro, intensita, commenti, data, area, centro_monitoraggio
                FROM rilevazioni
                WHERE area IN (
                \tSELECT id FROM aree_geografiche
                \tWHERE denominazione = ?
                \tAND stato = ?
                \tAND latitudine = ?
                \tAND longitudine = ?
                )
                """)) {
            st.setString(1, area.getDenominazione());
            st.setString(2, area.getStato());
            st.setDouble(3, area.getLatitudine());
            st.setDouble(4, area.getLongitudine());
            ResultSet rs = st.executeQuery();

            while(rs.next()){
                String category = rs.getString(1);
                int intensity = rs.getInt(2);
                String notes = rs.getString(3);
                Date date = rs.getDate(4);

                Parameter p = new Parameter(Parameter.Category.valueOf(category), intensity, notes, date, area);
                params.add(p);
            }
            rs.close();
        } catch (SQLException e) {

        }
        return params;
    }

    public ArrayList<Operator> select_all_operators(){
        ArrayList<Operator> operators = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT denominazione, stato, latitudine, longitudine FROM aree_geografiche;")) {
            while (rs.next()) {
                String denominazione = rs.getString(1);
                String stato = rs.getString(2);
                double latitudine = Double.parseDouble(rs.getString(3));
                double longitudine = Double.parseDouble(rs.getString(4));
                Area a = new Area(denominazione, stato, latitudine, longitudine);
                operators.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get areas from the database");
        }
        return operators;
    }
}
