package main;

import org.example.Area;
import org.example.MonitoringCenter;
import org.example.Operator;
import org.example.Parameter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
             ResultSet rs = st.executeQuery("SELECT denominazione, stato, latitudine, longitudine, id FROM aree_geografiche;")) {
            while (rs.next()) {
                String denominazione = rs.getString(1);
                String stato = rs.getString(2);
                double latitudine = Double.parseDouble(rs.getString(3));
                double longitudine = Double.parseDouble(rs.getString(4));
                int id = rs.getInt(5);
                Area a = new Area(denominazione, stato, latitudine, longitudine, id);
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

    /**
     * Select all operators information, except their monitoring center
     * @return
     */
    public HashMap<String, Operator> select_all_operators(){
        HashMap<String, Operator> operators = new HashMap<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT userId, nome, cognome, codice_fiscale, email, password FROM operatori;")) {
            while (rs.next()) {
                String userId = rs.getString(1);
                String nome = rs.getString(2);
                String cognome = rs.getString(3);
                String cf = rs.getString(4);
                String email = rs.getString(5);
                String password = rs.getString(6);
                Operator o = new Operator(userId, nome, cognome, cf, email, password, null);
                operators.put(userId, o);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get areas from the database");
        }
        return operators;
    }

    public Operator select_operator_by_id(String id){
        Operator operator = null;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM operatori WHERE userid = ?")){
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                String userId = rs.getString(1);
                String nome = rs.getString(2);
                String cognome = rs.getString(3);
                String cf = rs.getString(4);
                String email = rs.getString(5);
                String password = rs.getString(6);
                operator = new Operator(userId, nome, cognome, cf, email, password, null);
            }
            rs.close();
        } catch (SQLException e){
            System.err.println("Failed excute querry");
        }
        return operator;
    }

    public MonitoringCenter select_mc_by_name(String mcName){
        MonitoringCenter mc = null;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM centri_monitoraggio WHERE nome = ?")){
            st.setString(1, mcName);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                String name = rs.getString(1);
                String address = rs.getString(2);
                mc = new MonitoringCenter(name, address, null);
            }
            rs.close();
        } catch (SQLException e){
            System.err.println("Failed to excute querry");
            e.printStackTrace();
        }
        return mc;
    }

    public ArrayList<Area> select_aree_interesse_by_mc(String mcName){
        ArrayList<Area> areas = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement("SELECT (denominazione, stato, latitudine, longitudine)" +
                "\tFROM aree_geografiche" +
                "\tWHERE id IN" +
                "(SELECT id FROM aree_interesse WHERE centro_monitoraggio = ?)")){
            st.setString(1, mcName);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String denominazione = rs.getString(1);
                String stato = rs.getString(2);
                double latitudine = rs.getDouble(3);
                double longitudine = rs. getDouble(4);

                Area a = new Area(denominazione, stato, latitudine, longitudine);
                areas.add(a);
            }
        } catch (SQLException e){
            System.err.println("Failed to excute querry");
            e.printStackTrace();
        }
        return areas;
    }

    public boolean check_operator_exists(String id){
        boolean result = false;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM operatori WHERE userid = ?")) {
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            result = rs.next();
        } catch (SQLException e){
            System.err.println("Failed to execute query");
        }
        return result;
    }

    public boolean check_mc_exists(String mcName){
        boolean result = false;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM centri_monitoraggio WHERE nome = ?")) {
            st.setString(1, mcName);
            ResultSet rs = st.executeQuery();
            result = rs.next();
        } catch (SQLException e){
            System.err.println("Failed to execute query");
        }
        return result;
    }

    public boolean insert_operator(String userId, String nome, String cognome, String cf, String email, String password, String mc) {
        boolean inserted = true;
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO operatori VALUES (?, ?, ?, ?, ?, ?, ?)")){
            st.setString(1, userId);
            st.setString(2, nome);
            st.setString(3, cognome);
            st.setString(4, cf);
            st.setString(5, email);
            st.setString(6, password);
            st.setString(7, mc.isEmpty() ? null : mc);
            st.executeUpdate();
        } catch (SQLException e){
            inserted = false;
            System.err.println("Failed to register user");
            e.printStackTrace();
        }
        return inserted;
    }

    public boolean insert_area_interesse(int idArea, String mcName){
        boolean inserted = true;
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO aree_interesse VALUES(?, ?)")){
            st.setInt(1, idArea);
            st.setString(2, mcName);
        }   catch (SQLException e){
            inserted = false;
            System.err.println("Failed to insert area interesse");
            e.printStackTrace();
        }
        return inserted;
    }

    public boolean insert_mc(String mcName, String address){
        boolean inserted = true;
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO centri_monitoraggio VALUES (?, ?)")){
            st.setString(1, mcName);
            st.setString(2, address);
        }   catch (SQLException e){
            inserted = false;
            System.err.println("Failed to insert new monitoring center");
            e.printStackTrace();
        }
        return inserted;
    }
}
