package client;

import org.example.Area;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class AreaRegistry {
    private static ArrayList<Area> areas = null;

    public static ArrayList<Area> getAreas(){
        return areas;
    }

    public static void loadAreas(){
        try {
            areas = ConnectionManager.getCmServer().getAreas();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static Area getArea(String denominazione, String stato, Double latitudine, Double longitudine){
        Area areaToFind = new Area(denominazione, stato, latitudine, longitudine);
        for (Area a : areas){
            if (a.equals(areaToFind))
                    return a;
        }
        return null;
    }
}
