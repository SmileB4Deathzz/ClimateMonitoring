package org.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CMServerInterface extends Remote {
    //void login(String opId, String password) throws RemoteException;
    ArrayList<Area> getAreas() throws RemoteException;
    ArrayList<Parameter> getAreaParameters(Area a) throws RemoteException;
    //Parameter insertParameter(Parameter p, Area a) throws RemoteException;
    //void register(String nome, String cognome, String cf, String mail, String userId, String password, MonitoringCenter mc) throws RemoteException;
}
