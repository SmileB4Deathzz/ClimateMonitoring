package main;

import org.example.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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

    public ServerResponse register(String userId, String nome, String cognome, String cf, String mail, String password, String mc) {
        //check if operator already exists
        if (qe.check_operator_exists(userId)){
            return new ServerResponse(ServerResponse.Type.ERR, "Operator with this userId already exists");
        }
        //chec if mc with that name exists
        else if (!mc.isEmpty() && !qe.check_mc_exists(mc)){
            return new ServerResponse(ServerResponse.Type.ERR, "There are no monitoring centers with this name");
        }
        else {
            if (qe.insert_operator(userId, nome, cognome, cf, mail, password, mc))
                return new ServerResponse(ServerResponse.Type.INFO, "Operator registered");
            else
                return new ServerResponse(ServerResponse.Type.ERR, "Failed to register operator");
        }
    }

    public ServerResponse login (String userId, String password){
        if (qe.check_operator_exists(userId)){
            Operator op = qe.select_operator_by_id(userId);
            return new ServerResponse(op);
        }
        return new ServerResponse(ServerResponse.Type.ERR, "Incorrect userId or password");
    }

    public ServerResponse createMc(String mcName, String address, ArrayList<Area> areas){
        if (qe.check_mc_exists(mcName))
            return new ServerResponse(ServerResponse.Type.ERR, "Monitoring center with this name already exists");

        if (qe.insert_mc(mcName, address)) {
            for (Area a : areas){
                qe.insert_area_interesse(a.getId(), mcName);
            }
            return new ServerResponse(new MonitoringCenter(mcName, address, areas));
        }
        return new ServerResponse(ServerResponse.Type.ERR, "Failed to create monitoring center");
    }

    public ServerResponse
}
