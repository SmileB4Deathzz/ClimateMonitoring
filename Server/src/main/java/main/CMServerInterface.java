package main;

import client.Area;
import client.AreaList;
import client.MonitoringCenter;
import client.Parameter;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CMServerInterface extends Remote {
    //void login(String opId, String password) throws RemoteException;
    AreaList getAreas() throws RemoteException;
    //Parameter getParameter(Area a) throws RemoteException;
    //Parameter insertParameter(Parameter p, Area a) throws RemoteException;
    //void register(String nome, String cognome, String cf, String mail, String userId, String password, MonitoringCenter mc) throws RemoteException;
}
