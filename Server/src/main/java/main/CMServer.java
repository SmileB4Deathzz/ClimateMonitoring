package main;

import org.example.Area;
import org.example.CMServerInterface;
import org.example.Settings;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.postgresql.*;

/**
 * Hello world!
 *
 */
public class CMServer extends UnicastRemoteObject implements CMServerInterface {

    public CMServer() throws RemoteException {
    }

    public static void main(String[] args ) throws SQLException {
        Connection conn = new DBManager().connect("dbCM", "postgres", "buzica");
        try {
            new CMServer().startServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Area> getAreas() {
        ArrayList<Area> areas = new ArrayList<>();
        Connection conn= DBManager.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT denominazione, stato, latitudine, longitudine FROM aree_geografiche;");
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

    private void startServer() throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(Settings.PORT);
        try {
            registry.bind("ClimateMonitoring", this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");
    }
}
