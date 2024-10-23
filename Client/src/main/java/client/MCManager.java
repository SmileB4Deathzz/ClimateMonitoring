package client;

import java.io.*;
import java.util.HashMap;

/**
 * A class responsible for managing MonitoringCenter instances and persisting them to a file.
 * This class provides methods to save, retrieve, and interact with monitoring centers.
 */
public class MCManager {
    /** The file path to save and retrieve monitoring center data. */
    private final String filePath = "../data/CentriMonitoraggio.dati";

    /**
     * Class constructor.
     */
    public MCManager(){}

    /**
     * Saves a collection of monitoring centers to the designated file.
     *
     * @param mCenters The HashMap containing monitoring centers to be saved.
     */
    private void saveMCenters(HashMap<String, MonitoringCenter> mCenters){
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(mCenters);
        } catch (FileNotFoundException e) {
            new Dialog(Dialog.type.ERR, "Failed to serialize data");
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Retrieves a HashMap of monitoring centers from the designated file.
     *
     * @return A HashMap of monitoring centers loaded from the file.
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, MonitoringCenter> getMCenters(){
        HashMap<String, MonitoringCenter> mCenters = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            mCenters =  (HashMap<String, MonitoringCenter>) ois.readObject();

        } catch (IOException ioe) {
            if (ioe instanceof EOFException)
                return mCenters;
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return mCenters;
    }

    /**
     * Adds a new monitoring center to the collection and persists it to the file.
     *
     * @param mc The MonitoringCenter instance to be saved.
     * @return {@code true} if successfully added the Monitoring Center to the database
     * {@code false} otherwise
     */
    public boolean addMCenter(MonitoringCenter mc){
        try {
            HashMap<String, MonitoringCenter> mcenters = getMCenters();
            if (mcenters.containsKey(mc.getNome())) {
                new Dialog(Dialog.type.ERR, "Monitoring center with this name already exists");
                return false;
            }
            mcenters.put(mc.getNome(), mc);
            saveMCenters(mcenters);
        }
        catch (Exception e){
            new Dialog(Dialog.type.ERR, "Failed to add new monitoring center to the database");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Saves the changes made to a Monitoring Center
     *
     * @param mc the monitoring center to save
     */
    public void saveMCenter(MonitoringCenter mc){
        HashMap<String, MonitoringCenter> mcenters = getMCenters();
        if (!mcenters.containsKey(mc.getNome())) {
            new Dialog(Dialog.type.ERR, "Can't save Monitoring Center that hasn't been created yet");
            return;
        }
        mcenters.put(mc.getNome(), mc);
        saveMCenters(mcenters);
    }

    /**
     * Retrieves a monitoring center by its name.
     *
     * @param name The name of the monitoring center to retrieve.
     * @return The MonitoringCenter with the provided name, or {@code null} if not found.
     */
    public MonitoringCenter getMonitoringCenter(String name){
        HashMap<String, MonitoringCenter> mcs= getMCenters();
        return mcs.get(name);
    }

}
