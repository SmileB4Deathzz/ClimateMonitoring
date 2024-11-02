package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.text.StringEscapeUtils;

public class Main {
    private static Connection conn = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the database");
        String dbName = scanner.nextLine();

        System.out.println("Enter the username");
        String userName = scanner.nextLine();

        System.out.println("Enter the password");
        String password = scanner.nextLine();

        connect(dbName, userName, password);
        try {
            System.out.println("creating tables");
            createTables();
            System.out.println("Filling Geographical areas table");
            fillGeographicalAreasTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void connect(String dbName, String userName, String password) {
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
    }

    private static void createTables() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("""
                create table aree_geografiche (
                \tid SERIAL PRIMARY KEY,
                \tlatitudine numeric NOT NULL,
                \tlongitudine numeric NOT NULL,
                \tdenominazione varchar(50) NOT NULL,
                \tstato varchar(50) NOT NULL
                );

                create table centri_monitoraggio(
                \tid integer PRIMARY KEY,
                \tnome varchar(50),
                \tindirizzo varchar(50)
                );

                create table aree_interesse(
                \tarea integer NOT NULL,
                \tcentro_monitoraggio integer NOT NULL,
                \tPRIMARY KEY (area, centro_monitoraggio),
                \tFOREIGN KEY (area) references aree_geografiche(id)
                \t\tON DELETE CASCADE
                \t\tON UPDATE CASCADE,
                \tFOREIGN KEY (centro_monitoraggio) references centri_monitoraggio(id)
                \t\tON DELETE CASCADE
                \t\tON UPDATE CASCADE
                );

                create table operatori (
                \tuserId varchar(50) PRIMARY KEY,
                \tnome varchar(50) NOT NULL,
                \tcognome varchar(50) NOT NULL,
                \tcodice_fiscale varchar(50) NOT NULL,
                \temail varchar(255) NOT NULL,
                \tpassword varchar(255) NOT NULL,
                \tcentro_monitoraggio integer,
                \tFOREIGN KEY (centro_monitoraggio) references centri_monitoraggio(id)
                \t\tON DELETE SET NULL
                \t\tON UPDATE CASCADE
                );

                create table rilevazioni (
                \tparametro varchar(20) NOT NULL,
                \tintensita integer NOT NULL,
                \tcommenti varchar(1000),
                \tdata date,
                \tarea integer,
                \tcentro_monitoraggio integer,
                \tforeign key (area) references aree_geografiche(id)
                \t\tON UPDATE CASCADE,
                \tforeign key (centro_monitoraggio) references centri_monitoraggio (id)
                \t\tON DELETE SET NULL
                \t\tON UPDATE CASCADE
                );""");
        stmt.close();
    }

    private static void fillGeographicalAreasTable() throws SQLException {
        Statement stmt = conn.createStatement();
        String stmtString = "INSERT INTO aree_geografiche (latitudine, longitudine, denominazione, stato) VALUES ";
        URL filePath = Main.class.getClassLoader().getResource("CoordinateMonitoraggio.dati");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath.getFile()));
            String line;
            while ((line = reader.readLine()) != null) {

                String[] values = line.split("\t");
                String[] coordinates = values[2].trim().split(",");

                String denominazione = values[0].replace("'", "''");
                String stato = values[1].replace("'", "''");


                stmtString += "(" + coordinates[0] + ", " + coordinates[1] +
                        ", '" + denominazione + "', '" + stato + "'), ";
            }
            stmtString = stmtString.substring(0, stmtString.length()-2) + ";";
            System.out.println(stmtString);
            stmt.execute(stmtString);
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}