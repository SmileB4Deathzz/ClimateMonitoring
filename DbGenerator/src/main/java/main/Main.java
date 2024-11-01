package main;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

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
            createTables();
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
                \tid integer PRIMARY KEY,
                \tlatitudine numeric NOT NULL,
                \tlongitudine numeric NOT NULL,
                \tdenominazione varchar(30) NOT NULL,
                \tstato varchar(30) NOT NULL
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
                \tuserId varchar(20) PRIMARY KEY,
                \tnome varchar(20) NOT NULL,
                \tcognome varchar(20) NOT NULL,
                \tcodice_fiscale varchar(30) NOT NULL,
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

    private static void fillGeographicalAreasTable(){

    }
}