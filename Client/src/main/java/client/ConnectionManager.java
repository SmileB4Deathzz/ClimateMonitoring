package client;

import org.example.CMServerInterface;
import org.example.Settings;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ConnectionManager extends UnicastRemoteObject {
    private static CMServerInterface cmServer = null;

    protected ConnectionManager() throws RemoteException {
    }

    public static CMServerInterface getCmServer() {
        return cmServer;
    }

    public static void setCmServer(CMServerInterface cmServer) {
        ConnectionManager.cmServer = cmServer;
    }
}
