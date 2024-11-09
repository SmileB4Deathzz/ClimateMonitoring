package main;

import org.example.Area;
import org.example.MonitoringCenter;
import org.example.Operator;
import org.example.Parameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class QueryExecutor {
    private Connection conn = null;

    public QueryExecutor(String dbName, String username, String password) {
        this.conn = connect();
    }

    private static final String CONFIG_FILE_NAME = "dbconfig.cfg"; // Only the file name

    public Connection connect() {
        Properties props = new Properties();
        String dbName;

        // Attempt to load the config file from resources
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (inputStream != null) {
                // Load credentials from config file
                props.load(inputStream);
                dbName = props.getProperty("dbName");
            } else {
                // Config file not found, prompt for credentials
                props = promptUserForCredentials();
                dbName = props.getProperty("dbName");
                savePropertiesToFile(props);  // Save properties for future use
            }
        } catch (IOException e) {
            System.out.println("Error loading configuration file.");
            throw new RuntimeException(e);
        }

        // Connect to the database
        String url = "jdbc:postgresql://localhost/" + dbName;
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            throw new RuntimeException(e);
        }
        return conn;
    }

    // Prompt the user for database name, username, and password if config file is missing
    private Properties promptUserForCredentials() {
        Scanner scanner = new Scanner(System.in);
        Properties props = new Properties();

        System.out.print("Enter database name: ");
        props.setProperty("dbName", scanner.nextLine());

        System.out.print("Enter database username: ");
        props.setProperty("user", scanner.nextLine());

        System.out.print("Enter database password: ");
        props.setProperty("password", scanner.nextLine());

        return props;
    }

    // Save properties to the config file for future use
    private void savePropertiesToFile(Properties props) {
        // Locate the resources directory to save the config file
        try {
            File configFile = new File(getClass().getClassLoader().getResource("").toURI().getPath(), CONFIG_FILE_NAME);
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                props.store(fos, "Database Configuration");
                System.out.println("Configuration saved to " + configFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Failed to save configuration file.");
            throw new RuntimeException(e);
        }
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

                Parameter p = new Parameter(Parameter.Category.valueOf(category), intensity, notes, date, area, null);
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
                String mcName = rs.getString(7);

                MonitoringCenter mc = null;
                if (mcName != null) {
                    mc = this.select_mc_by_name(mcName);
                    mc.addAreas(this.select_aree_interesse_by_mc(mcName));
                }
                operator = new Operator(userId, nome, cognome, cf, email, password, mc);
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
        try (PreparedStatement st = conn.prepareStatement("SELECT denominazione, stato, latitudine, longitudine" +
                "\tFROM aree_geografiche" +
                "\tWHERE id IN" +
                "(SELECT area FROM aree_interesse WHERE centro_monitoraggio = ?)")){
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

    public boolean check_operator_login(String userId, String password){
        boolean result = false;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM operatori WHERE userid = ? AND password = ?")) {
            st.setString(1, userId);
            st.setString(2, password);
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
            st.executeUpdate();
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
            st.executeUpdate();
        }   catch (SQLException e){
            inserted = false;
            System.err.println("Failed to insert new monitoring center");
            e.printStackTrace();
        }
        return inserted;
    }

    public void insert_areas_to_mc(String mcName, ArrayList<Area> areas){
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO aree_interesse VALUES (?, ?)")){
            for (Area area : areas){
                st.setInt(1, area.getId());
                st.setString(2, mcName);
                st.addBatch();
            }
            st.executeBatch();
        } catch (SQLException e) {
            System.err.println("Failed to insert areas");
            e.printStackTrace();
        }
    }

    public boolean insert_parameter(Parameter param){
        boolean inserted = false;
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO rilevazioni VALUES (?, ?, ?, ?, ?, ?)")){
            st.setString(1, param.getCategory().toString());
            st.setInt(2, param.getScore());
            st.setString(3, param.getNotes().isEmpty() ? null : param.getNotes());
            st.setDate(4, new java.sql.Date(param.getDate().getTime()));
            st.setInt(5, param.getArea().getId());
            st.setString(6, param.getMc().getNome());
            st.executeUpdate();
            inserted = true;
        } catch (SQLException e){
            System.err.println("Failed to insert Parameter");
            e.printStackTrace();
        }
        return inserted;
    }

    public void update_operator_mc(String userId, String mcName){
        try (PreparedStatement st = conn.prepareStatement("UPDATE operatori SET centro_monitoraggio = ? WHERE userid = ?")){
            st.setString(1, mcName);
            st.setString(2, userId);
            st.executeUpdate();
        } catch (SQLException e){
            System.err.println("failed to execute query");
            e.printStackTrace();
        }
    }
}
