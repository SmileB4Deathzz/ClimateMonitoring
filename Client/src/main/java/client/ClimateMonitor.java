package client;

import com.formdev.flatlaf.FlatIntelliJLaf;
import org.example.CMServerInterface;
import org.example.Settings;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Progetto laboratorio A: "Climate Monitoring", anno 2022-2023
 * @author Vartic Cristian, Matricola 748689
 * @version 1.0
 */
public class ClimateMonitor extends UnicastRemoteObject {
    protected ClimateMonitor() throws RemoteException {
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, SQLException, RemoteException {
        try {
            new ClimateMonitor().startClient();
        } catch (Exception e) {
            new Dialog(Dialog.type.ERR, "Failed to connect to server");
            throw new RuntimeException(e);
        }

        AreaRegistry.loadAreas();
        UIManager.setLookAndFeel(new FlatIntelliJLaf());

        new MainView();
    }

    private void startClient() throws Exception {
        Registry registry;
        registry = LocateRegistry.getRegistry(Settings.SERVER_NAME,
                Settings.PORT);
        CMServerInterface cmServer = (CMServerInterface) registry.lookup("ClimateMonitoring");
        ConnectionManager.setCmServer(cmServer);
    }
}