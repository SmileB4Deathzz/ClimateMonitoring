package main;

import org.example.Area;
import org.example.CMServerInterface;
import org.example.Parameter;
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
    private QueryExecutor qe = null;

    public CMServer() throws RemoteException {
    }

    public static void main(String[] args ) throws SQLException {
        try {
            new CMServer().startServer();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void startServer() throws RemoteException {
        qe = new QueryExecutor("dbCM", "postgres", "buzica");
        Registry registry = LocateRegistry.createRegistry(Settings.PORT);
        try {
            registry.bind("ClimateMonitoring", this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Server ready");
    }

    public ArrayList<Area> getAreas() {
        return qe.select_all_geographic_areas();
    }

    public ArrayList<Parameter> getAreaParameters(Area area){
        return qe.select_area_parameters(area);
    }
}
